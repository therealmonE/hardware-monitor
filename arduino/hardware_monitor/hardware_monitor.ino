#include <LiquidCrystal_I2C.h>

LiquidCrystal_I2C lcd(0x27, 20, 4);

#define printByte(args)  write(args);

const char* USAGES_SEPARATOR = ";";
const char CODE_VALUE_SEPARATOR = ':';
const char MESSAGE_END = '.';
const char MESSAGE_START = '!';

const int CPU_USAGE = 0;
const int CPU_TEMP = 1;
const int GPU_USAGE = 2;
const int GPU_TEMP = 3;

const uint8_t LEFT_EMPTY_CELL = 1;
const uint8_t FILLED_CELL = 2;
const uint8_t EMPTY_CELL = 3;
const uint8_t RIGHT_EMPTY_CELL = 4;
const uint8_t DEGREE = 5;

struct Usages {
  String cpu_usage;
  String cpu_temp;
  String gpu_usage;
  String gpu_temp;
};


uint8_t left_empty_cell[8] = {
  0b11111,
  0b10000,
  0b10000,
  0b10000,
  0b10000,
  0b10000,
  0b10000,
  0b11111
};

uint8_t filled_cell[8] = {
  0b11111,
  0b11111,
  0b11111,
  0b11111,
  0b11111,
  0b11111,
  0b11111,
  0b11111
};

uint8_t empty_cell[8] = {
  0b11111,
  0b00000,
  0b00000,
  0b00000,
  0b00000,
  0b00000,
  0b00000,
  0b11111
};

uint8_t right_empty_cell[8] = {
  0b11111,
  0b00001,
  0b00001,
  0b00001,
  0b00001,
  0b00001,
  0b00001,
  0b11111
};

uint8_t degree[8] = {
  0b11100,
  0b10100,
  0b11100,
  0b00000,
  0b00000,
  0b00000,
  0b00000,
  0b00000
};

uint8_t plot0[3] = {LEFT_EMPTY_CELL, EMPTY_CELL, RIGHT_EMPTY_CELL};
uint8_t plot1[3] = {FILLED_CELL, EMPTY_CELL, RIGHT_EMPTY_CELL};
uint8_t plot2[3] = {FILLED_CELL, FILLED_CELL, RIGHT_EMPTY_CELL};
uint8_t plot3[3] = {FILLED_CELL, FILLED_CELL, FILLED_CELL};

void setup() {
  Serial.begin(9600);
  lcd.init();
  lcd.backlight();
  lcd.clear();
  createPlotChars();
}


void createPlotChars() {
  lcd.createChar(LEFT_EMPTY_CELL, left_empty_cell);
  lcd.createChar(FILLED_CELL, filled_cell);
  lcd.createChar(EMPTY_CELL, empty_cell);
  lcd.createChar(RIGHT_EMPTY_CELL, right_empty_cell);
  lcd.createChar(DEGREE, degree);
}

void loop() {
  while (Serial.available()) {
    if (Serial.read() == MESSAGE_START) {
      Usages usages = readUsages();
      printUsages(&usages);
    }
  }
}

Usages readUsages() {
  String usagesString = Serial.readStringUntil(MESSAGE_END);

  return parseUsages(usagesString);
}

Usages parseUsages(String usagesString) {
  Usages usages = {};

  char chars[usagesString.length()];
  usagesString.toCharArray(chars, usagesString.length());
  char* usagePointer = strtok(chars, USAGES_SEPARATOR);

  while (usagePointer != 0) {
    String usage = String(usagePointer);
    int codeValueSeparator = usage.indexOf(CODE_VALUE_SEPARATOR);
    int code = usage.substring(0, codeValueSeparator).toInt();
    String value = usage.substring(codeValueSeparator + 1, usage.length());

    fillUsages(&usages, code, value);

    usagePointer = strtok(0, USAGES_SEPARATOR);
  }

  return usages;
}

void fillUsages(Usages* usages, int code, String value) {
  switch (code) {
    case CPU_USAGE: usages->cpu_usage = value; break;
    case CPU_TEMP: usages->cpu_temp = value; break;
    case GPU_USAGE: usages->gpu_usage = value; break;
    case GPU_TEMP: usages->gpu_temp = value; break;
  }
}

void printUsages(Usages* usages) {
  lcd.clear();
  lcd.setCursor(0, 0);
  printUsages("CPU", usages->cpu_usage, usages->cpu_temp);
  lcd.setCursor(0, 1);
  printUsages("GPU", usages->gpu_usage, usages->gpu_temp);
}

void printUsages(String label, String usage, String temp) {
  lcd.print(label + " ");
  lcd.print(usage + "%");
  printPlot(atoi(usage.c_str()));
  lcd.print(temp);
  lcd.printByte(DEGREE);
  printPlot(atoi(temp.c_str()));
}

void printPlot(int value) {
  if (value < 25) {
    printPlot(plot0);
  } else if (value < 50) {
    printPlot(plot1);
  } else if (value < 75) {
    printPlot(plot2);
  } else {
    printPlot(plot3);
  }
}

void printPlot(uint8_t plot[]) {
  for (int i = 0; i < 3; i++) {
    lcd.printByte(plot[i]);    
  }
}
