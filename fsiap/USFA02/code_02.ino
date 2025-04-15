#include <DHT.h>

// Definições do sensor de temperatura/humidade
#define DHTPIN 2    // Pino onde o sensor DHT está conectado
#define DHTTYPE DHT11 // Tipo do sensor (DHT11 ou DHT22)

// Inicialização do sensor
DHT dht(DHTPIN, DHTTYPE);

// Definições dos LEDs
#define TEMP_LED 3   // LED para o sensor de temperatura
#define HUMID_LED 4  // LED para o sensor de umidade

// Variáveis para armazenar os valores iniciais
float initialTemp = 0.0;
float initialHumidity = 0.0;

// Variável para controlar a exibição do cabeçalho
int rowCount = 0;

// Tempo máximo de execução em milissegundos (15 minutos)
unsigned long maxTime = 900000;  // 15 minutos em milissegundos
unsigned long startTime;  // Variável para armazenar o tempo de início

// Variável de controle para indicar se o sistema já foi finalizado
bool isFinished = false;

void setup() {
  // Inicialização do monitor serial
  Serial.begin(9600);
  Serial.println("Iniciando o sistema...");

  // Inicialização do sensor DHT
  dht.begin();

  // Configuração dos LEDs como saída
  pinMode(TEMP_LED, OUTPUT);
  pinMode(HUMID_LED, OUTPUT);

  // Calcular médias para valores iniciais
  Serial.println("Calculando os valores iniciais...");
  int numReadings = 0;
  float tempSum = 0.0;
  float humiditySum = 0.0;

  startTime = millis();  // Armazena o tempo de início
  unsigned long elapsedTime = 0;  // Variável para controlar o tempo de execução

  while (elapsedTime < 10000) { // Loop por 10 segundos
    float temp = dht.readTemperature();
    float humidity = dht.readHumidity();

    if (!isnan(temp) && !isnan(humidity)) { // Adiciona leituras válidas
      tempSum += temp;
      humiditySum += humidity;
      numReadings++;
    }
    delay(500); // Lê a cada 0,5 segundos
    elapsedTime = millis() - startTime; // Atualiza o tempo decorrido
  }

  if (numReadings > 0) { // Calcula as médias
    initialTemp = tempSum / numReadings;
    initialHumidity = humiditySum / numReadings;
    Serial.println("Valores iniciais calculados:");
    Serial.print("Temperatura Inicial Média: ");
    Serial.print(initialTemp, 1);
    Serial.println(" °C");
    Serial.print("Humidade Inicial Média: ");
    Serial.print(initialHumidity, 1);
    Serial.println(" %");
  } else {
    Serial.println("Erro: Não foi possível calcular os valores iniciais. Verifique o sensor.");
  }

  // Exibe cabeçalho da tabela
  printHeader();
}

void loop() {
  // Verifica se o tempo limite foi atingido
  if (millis() - startTime >= maxTime) {
    if (!isFinished) { // Exibe a mensagem de término apenas uma vez
      Serial.println("+---------------------+------------------+-------------+----------+----------+---------+---------+");
      Serial.println("");
      Serial.println("15 minutos de leituras concluídos. Parando o sistema.");
      isFinished = true;
    }
    return;  // Não executa mais leituras, mas mantém o programa funcionando
  }

  // Lê os valores atuais de temperatura e humidade
  float currentTemp = dht.readTemperature();
  float currentHumidity = dht.readHumidity();

  // Verifica se as leituras são válidas
  if (isnan(currentTemp) || isnan(currentHumidity)) {
    Serial.println("Erro ao ler os sensores.");
    return;
  }

  // Calcula o tempo decorrido
  unsigned long elapsedTime = millis() - startTime;
  unsigned long minutes = (elapsedTime / 60000); // Converte para minutos
  unsigned long seconds = (elapsedTime % 60000) / 1000; // Converte para segundos

  // Atualiza os LEDs conforme as condições
  digitalWrite(TEMP_LED, currentTemp >= initialTemp + 5 ? HIGH : LOW);
  digitalWrite(HUMID_LED, currentHumidity >= initialHumidity + 5 ? HIGH : LOW);

  // Exibe os valores na tabela
  Serial.print("| ");
  Serial.print(initialTemp, 1);
  Serial.print(" °C             | ");
  Serial.print(initialHumidity, 1);
  Serial.print(" %           | ");

  Serial.print(currentTemp, 1);
  Serial.print(" °C     | ");
  Serial.print(currentTemp >= initialTemp + 5 ? "ON " : "OFF");
  Serial.print("      | ");
  Serial.print(currentHumidity, 1);
  Serial.print(" %   | ");
  Serial.print(currentHumidity >= initialHumidity + 5 ? "ON " : "OFF");
  Serial.print("     | ");

  // Exibe o tempo decorrido (minutos:segundos)
  Serial.print(minutes);
  Serial.print(":");
  if (seconds < 10) {
    Serial.print("0");  // Adiciona o zero à frente dos segundos menores que 10
  }
  Serial.print(seconds);
  Serial.println("    |");

  // Incrementa contador de linhas
  rowCount++;

  // Exibe o cabeçalho a cada 20 linhas para facilitar a leitura
  if (rowCount >= 20) {
    printHeader();
    rowCount = 0;
  }

  // Intervalo entre as leituras (1 segundo)
  delay(1000);
}

void printHeader() {
  Serial.println("+---------------------+------------------+-------------+----------+----------+---------+---------+");
  Serial.println("| Temperatura Inicial | Humidade Inicial | Temperatura | LED Temp | Humidade | LED Hum |  Tempo  |");
  Serial.println("+---------------------+------------------+-------------+----------+----------+---------+---------+");
}
