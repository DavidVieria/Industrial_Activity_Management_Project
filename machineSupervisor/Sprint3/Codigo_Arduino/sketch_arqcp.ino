#include <arduino-timer.h>
#include <DHT.h>    // Biblioteca para o sensor DHT
#include <Arduino.h>

// Configurações de temporizadores
#define TOGGLE_LED_PERIOD 1000 // Alterna o LED integrado a cada 1 segundo
#define READ_PERIOD 7000       // Lê temperatura e humidade a cada 7 segundos
#define DATA_LENGTH 100        // Tamanho máximo para o token gerado pelo sensor de hum e temp 

// Configurações dos pinos
#define LED_0 0
#define LED_1 1
#define LED_2 2
#define LED_3 3
#define LED_4 4
#define BUILTIN_LED LED_BUILTIN
#define DHTPIN 28
#define DHTTYPE DHT11

// Variáveis para as leituras iniciais
float initialTemperature = -1;
float initialHumidity = -1;

// Statuis da máquina atual
const char* tempLedStatus = "OFF";  


// Variáveis globais
auto timer = timer_create_default();  // Instância do temporizador
DHT dht(DHTPIN, DHTTYPE);

int ledPins[5] = {LED_4, LED_3, LED_2, LED_1, LED_0};  // LEDs binários
String command = ""; // Comando recebido

// Funções
void send_data(float value1, float value2) {
  char buffer[DATA_LENGTH];
  snprintf(buffer, DATA_LENGTH - 1, "TEMP&unit:celsius&value:%2.0f#HUM&unit:percentage&value:%2.0f", value1, value2); // 38 + 17 = 55 chars + 35 chars = 90 chars
  Serial.println(buffer);
}



// Alterna o estado do LED integrado
bool toggleLED(void *) {
  digitalWrite(BUILTIN_LED, !digitalRead(BUILTIN_LED));
  return true; // Mantém o temporizador ativo
}

bool read_data(void *) {
  float temp = dht.readTemperature();
  float hum = dht.readHumidity();

  //check_led(temp, hum);

  if (!isnan(temp) && !isnan(hum)) {
    send_data(temp, hum);
  }

  return true;
}

// Processa os comandos recebidos
void processCommand(String cmd) {
  if (cmd.startsWith("OFF")) {
    turn_off_leds();
    delay(2000);
  } else if (cmd.startsWith("ON")) {
    String binaryCommand = cmd.substring(3); // Extrai a parte binária do comando
    executeOperation(binaryCommand);
  } else {
    Serial.println("Invalid command.");
  }
}

// Desliga todos os LEDs e o LED integrado
void turn_off_leds() {
  digitalWrite(BUILTIN_LED, LOW);  // Desliga o LED integrado
  for (int i = 0; i < 5; i++) {
    digitalWrite(ledPins[i], LOW);  // Desliga LEDs binários
  }
}

// Executa a operação de acordo com o comando
void executeOperation(String binaryCommand) {
  digitalWrite(BUILTIN_LED, HIGH); // Indica "pronto"

  // Lê e envia os dados do sensor
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();
  if (!isnan(temperature) && !isnan(humidity)) {
    send_data(temperature, humidity);
  }

  // Liga LEDs binários conforme o comando
  turn_on_leds(binaryCommand);

  // Aguarda 2 segundos e desliga LEDs
  delay(2000);
  turn_off_leds();
  delay(400);
}

// Configura LEDs binários com base no comando
void turn_on_leds(String binaryCommand) {
  int index = 0;
  while (binaryCommand.length() > 0 && index < 5) {
    int value = binaryCommand.substring(0, 1).toInt();  // Primeiro valor do comando
    digitalWrite(ledPins[index], value);
    binaryCommand = binaryCommand.substring(2);         // Remove "x," do comando
    index++;
  }
}

void setup() {
  Serial.begin(9600);  // Comunicação serial
  dht.begin();         // Inicializa o sensor DHT

  // Configuração de LEDs
  pinMode(BUILTIN_LED, OUTPUT);
  for (int i = 0; i < 5; i++) {
    pinMode(ledPins[i], OUTPUT);
  }
  // Configurar o sensor Hum e Temp
  initialTemperature = dht.readTemperature();
  initialHumidity = dht.readHumidity();

  send_data(initialTemperature, initialHumidity);

  // Inicia os temporizadores
  timer.every(TOGGLE_LED_PERIOD, toggleLED); // Alterna o LED integrado
  ////timer.every(READ_PERIOD, read_data);  // Lê temperatura e humidade

  Serial.println("Machine ready. Waiting for command...");
  digitalWrite(BUILTIN_LED, HIGH);  // LED integrado indica "pronto"
}

void loop() {
  timer.tick();  // Atualiza o temporizador

  // Verifica se há comando recebido
  if (Serial.available() > 0) {
    command = Serial.readStringUntil('\n');  // Lê comando da Serial
    command.trim();                          // Remove espaços extras
    processCommand(command);                 // Processa o comando
  }
}