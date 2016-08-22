public class Calculator {
  private static final char NO_OP = '\0';
  private static final char PLUS = '+';
  private static final char SUBTRACT = '-';
  private static final char MULTIPLY = '*';
  private static final char DIVIDE = '/';

  private float  number1 = 0.0F;
  private char   operator = NO_OP;

  public String opEquals(String number) {
    float result;
    result = performOperation(parseNumber(number));
    operator = NO_OP;
    return Float.toString((number1 = result));
  }

  public String opAdd(String number) {
    float result;

    result = performOperation(parseNumber(number));
    operator = PLUS;
    return Float.toString((number1 = result));
  }

  public String opSubtract(String number) {
    float result;

    result = performOperation(parseNumber(number));
    operator = SUBTRACT;
    return Float.toString((number1 = result));
  }

  public String opMultiply(String number) {
    float result;

    result = performOperation(parseNumber(number));
    operator = MULTIPLY;
    return Float.toString((number1 = result));
  }

  public String opDivide(String number) {
    float result;

    result = performOperation(parseNumber(number));
    operator = DIVIDE;
    return Float.toString((number1 = result));
  }

  private float performOperation(float number2) {
    float result = 0.0F;

    switch (operator) {
    case NO_OP:
      result = number2;
      break;
    case PLUS:
      result = number1 + number2;
      break;
    case SUBTRACT:
      result = number1 - number2;
      break;
    case MULTIPLY:
      result = number1 * number2;
      break;
    case DIVIDE:
      result = number1 / number2;
      break;
    }

    return result;
  }

  protected static float parseNumber(String number) {
    float real_number;

    try {
      real_number = Float.parseFloat(number);
    } catch (NumberFormatException e) {
      real_number = Float.NaN;
    }

    return real_number;
  }
}