import java.io.*;

/**Написать консольное Java приложение, выполняющие набор заданных арифметических операций.
        * Операции:
        * 1) Сложение 2х и более чисел
        * 2) Умножение 2х и более чисел
        * 3) Умножение первых 2х чисел и сложение с 3м числом
        * Приложение может получать параметры либо из файла, либо вводом из консоли.
        * Приложение может выводить результат либо в файл, либо в консоль.
        * Режим работы определяется аргументами командной строки.
        * Если значение аргумента задано как "-", то это означает работу с консолью, иначе с файлом.
 **/

public class Solution {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        writeMessage("Введите даныые в виде: \"test-app - -\" \n" +
                "Если данные необходимо брать из файла (и\\или загружать в файл), то вместо \"-\" введите путь файла");
        String testApp = readLine();
        while (!testApp.equals("exit")) {
            if (testApp.startsWith("test-app"))
                break;
            else writeMessage("Вы ввели неверные данные. Повторите попытку или введите \"exit\" для завершения");
            testApp = readLine();
        }

        String[] split = testApp.split(" ", 3);

        String arguments = readArguments(split[1]);

        if (split[2].equals("-"))
            writeMessage("Ответ: " + getResult(arguments));
        else writeToFile(split[2], getResult(arguments));
        close();
    }

    private static String readArguments(String filename) throws IOException {
        if (filename.equals("-")) {
            writeMessage("Введите тип оперции и перечислите аргументы, разделяя проблелом\n" +
                    "Список доступных операций:\n" +
                    "\t add - сумма всех аргументов, не менее двух\n" +
                    "\t mul - произведение всех аргументов, не менее двух\n" +
                    "\t aam - сумма первых двух аргументов, умноженная на третий");
            return readLine();
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String result = reader.readLine();
            writeMessage(String.format("В файле %s строка: %s", filename, result));
            reader.close();
            return result;
        }
    }

    private static int getResult(String argument) {
        String[] splitArguments = argument.split(" ");
        int result;
        switch (splitArguments[0]) {
            case "add":
                result = add(getArguments(splitArguments));
                break;
            case "mul":
                result = mul(getArguments(splitArguments));
                break;
            case "aam":
                result = addAndMul(getArguments(splitArguments));
                break;
            default:
                writeMessage("Вы ввели неверные данные");
                throw new IllegalArgumentException();
        }
        return result;
    }

    private static int[] getArguments(String[] arguments) {
        int[] result = new int[arguments.length - 1];
        for (int i = 0; i < result.length; i++)
            result[i] = Integer.parseInt(arguments[i + 1]);
        return result;
    }

    private static void writeToFile(String fileName, int result) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("Ответ: " + result);
        writer.close();
    }


    //методы выполняющие операции
    private static int add(int... args) {
        int result = 0;
        for (int arg : args) {
            result += arg;
        }
        return result;
    }

    private static int mul(int... args) {
        int result = 1;
        for (int arg : args) {
            result *= arg;
        }
        return result;
    }

    private static int addAndMul(int... args) {
        return (args[0] + args[1]) * args[2];
    }

    //методы для работы с консолью
    private static void writeMessage(String message) {
        System.out.println(message);
    }

    private static String readLine() throws IOException {
        return reader.readLine();
    }

    public static void close() throws IOException {
        reader.close();
    }
}
