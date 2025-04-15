#include <Adafruit_Sensor.h>
#include <DHT.h>

#define DHTPIN 28            // Pino de dados do sensor DHT11
#define DHTTYPE DHT11        // Tipo do sensor
#define TEMP_LED 2           // Pino do LED de temperatura (GPIO11)
#define HUM_LED 0            // Pino do LED de humidade (GPIO13)
#define GAS_SENSOR_ANALOG_PIN 26  // Pino do sensor de gás (GPIO26)
#define FAN_LED 14           // Pino do LED que representa a ventoinha (GPIO15)

DHT dht(DHTPIN, DHTTYPE);

float initialTemp;
float initialHumidity;
int initialGasLevel;
unsigned long startTime;
unsigned long lastUpdate = 0;

const int PROPANE_THRESHOLD = 45; // Limite analógico para detectar gás propano
const unsigned long SAMPLE_INTERVAL = 2000; // Intervalo de amostragem (2 segundos)

void setup() {
    Serial.begin(9600);
    delay(1000);  // Pausa para permitir que o Serial Monitor se conecte

    dht.begin();

    pinMode(TEMP_LED, OUTPUT);
    pinMode(HUM_LED, OUTPUT);
    pinMode(FAN_LED, OUTPUT);
    pinMode(GAS_SENSOR_ANALOG_PIN, INPUT);

    // Leitura inicial de sensores
    initialTemp = dht.readTemperature();
    initialHumidity = dht.readHumidity();
    initialGasLevel = analogRead(GAS_SENSOR_ANALOG_PIN);  // Leitura inicial do sensor de gás
    startTime = millis();

    // Escreve cabeçalho no Serial Monitor
    Serial.println("Monitorização de temperatura, humidade e gás:");
    Serial.print("Temperatura inicial: ");
    Serial.print(initialTemp);
    Serial.println(" °C");

    Serial.print("Humidade inicial: ");
    Serial.print(initialHumidity);
    Serial.println(" %");

    Serial.print("Nível inicial de gás: ");
    Serial.println(initialGasLevel);
}

void loop() {
    float temperature = dht.readTemperature();
    float humidity = dht.readHumidity();
    int gasLevel = analogRead(GAS_SENSOR_ANALOG_PIN);  // Leitura atual do sensor de gás
    unsigned long currentTime = millis();

    if (isnan(temperature) || isnan(humidity)) {
        Serial.println("Falha na leitura do sensor!");
        return;
    }

    // Lógica para controle de temperatura
    if (temperature >= initialTemp + 5) {
        Serial.println("Temperatura excedeu o limite (5ºC acima). Ativando ventoinha...");
        controlFan(5000, 5000);  // Exaustor 5s + Ventilação 5s
    } else {
        digitalWrite(TEMP_LED, LOW);
    }

    // Lógica para controle de humidade
    if (humidity >= initialHumidity * 1.05) {     
        Serial.println("Humidade excedeu o limite (5% acima). Ativando ventoinha...");
        controlFan(10000, 10000);  // Ventilação 10s + Exaustor 10s
    } else {
        digitalWrite(HUM_LED, LOW);
    }

    // Lógica para detecção de gás (2% acima do inicial)
    if (gasLevel >= initialGasLevel * 1.02) {
        Serial.println("Gás propano detectado! Ativando ambas as ventoinhas...");
        activateFan(10000);  // Ambas as ventoinhas por 10s
    }

    // Lógica para mudanças súbitas (>30% por minuto)
    if (lastUpdate == 0 || (currentTime - lastUpdate) >= SAMPLE_INTERVAL) {
        detectSuddenChanges(temperature, humidity, gasLevel);
        lastUpdate = currentTime;
    }

    // Envia dados para o Serial Monitor
    Serial.print((currentTime - startTime) / 1000);  // Tempo em segundos
    Serial.print(", Temperatura: ");
    Serial.print(temperature);                      // Temperatura
    Serial.print(" °C, Humidade: ");
    Serial.print(humidity);                         // Humidade
    Serial.print(" %, Gás: ");
    Serial.print(gasLevel);                         // Nível de gás
    Serial.println();

    delay(SAMPLE_INTERVAL);  // Espera antes da próxima leitura
}

// Função para controlar o LED da ventoinha para operações sequenciais
void controlFan(int firstDuration, int secondDuration) {
    digitalWrite(TEMP_LED, HIGH);
    delay(firstDuration);  // Ventilação ou Exaustão por tempo específico
    digitalWrite(TEMP_LED, LOW);
    digitalWrite(HUM_LED, HIGH);
    delay(secondDuration); // Ventilação ou Exaustão por tempo específico
    digitalWrite(HUM_LED, LOW);
}

// Função para ativar ambas as ventoinhas por um único período
void activateFan(int duration) {
    digitalWrite(FAN_LED, HIGH);
    delay(duration);  // Ambas as ventoinhas ativadas
    digitalWrite(FAN_LED, LOW);
}

// Função para detectar mudanças súbitas em sensores
void detectSuddenChanges(float temperature, float humidity, int gasLevel) {
    static float lastTemp = initialTemp;
    static float lastHumidity = initialHumidity;
    static int lastGasLevel = initialGasLevel;

    float tempChange = (temperature - lastTemp) / lastTemp * 100;
    float humidityChange = (humidity - lastHumidity) / lastHumidity * 100;
    float gasChange = (gasLevel - lastGasLevel) / (float)lastGasLevel * 100;

    if (tempChange > 30 || humidityChange > 30 || gasChange > 30) {
        Serial.println("Mudança súbita detectada! Ativando ambas as ventoinhas...");
        activateFan(10000);
    }

    lastTemp = temperature;
    lastHumidity = humidity;
    lastGasLevel = gasLevel;
}
