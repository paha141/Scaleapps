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

public class Solution implements Closeable {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private BufferedWriter writer;

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();
        solution.writeMessage("Введите даныые в виде: \"test-app - -\" \n" +
                "Если данные необходимо брать из файла (и\\или загружать в файл), то вместо \"-\" введите путь файла");
        String testApp = solution.readLine();
        while (!testApp.equals("exit")) {
            if (testApp.startsWith("test-app"))
                break;
            else solution.writeMessage("Вы ввели неверные данные. Повторите попытку или введите \"exit\" для завершения");
            testApp = solution.readLine();
        }

        String[] split = testApp.split(" ", 3);

        if (!split[2].equals("-")) solution.writer = new BufferedWriter(new FileWriter(split[2]));
        if (split[1].equals("-")) solution.readFromConsole(solution.writer);
        else solution.readFromFile(split[1], solution.writer);

        solution.close();
    }

    //чтение данных с консоли и запись результата
    private void readFromConsole(Writer out) throws IOException {
            writeMessage("Введите тип оперции и перечислите аргументы, разделяя проблелом\n" +
                    "Список доступных операций:\n" +
                    "\t add - сумма всех аргументов, не менее двух\n" +
                    "\t mul - произведение всех аргументов, не менее двух\n" +
                    "\t aam - сумма первых двух аргументов, умноженная на третий\n" +
                    "\t exit - для выхода из программы. Аргуметы перечислять не нужно");
            String argument = readLine();

            while (!argument.equals("exit")) {
                try {
                    int result = getResult(argument);
                    writeResult(out, String.format("Для строки %s ответ: %d\n", argument, result));
                } catch (WrongArgumentException e) {
                    writeMessage(e.getMessage());
                }
                argument = readLine();
            }
    }

    //чтение данных из файла и запись результата
    private void readFromFile(String fileName, Writer out) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        while (reader.ready()) {
            String argument = reader.readLine();
            writeMessage(String.format("В файле %s строка: %s", fileName, argument));
            try {
                int result = getResult(argument);
                out.write(String.format("Для строки %s ответ: %d\n", argument, result));
            } catch (WrongArgumentException e) {
                writeMessage(e.getMessage());
            }
        }
        reader.close();
    }

    //запись резульатата в файл или консоль
    private void writeResult(Writer out, String result) throws IOException {
        if (writer == null)
            writeMessage(result);
        else {
            out.write(result);
        }
    }

    //получение результата
    private static int getResult(String argument) throws WrongArgumentException {
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
                throw new WrongArgumentException(splitArguments[0] + " - неподходящая операция");
        }
        return result;
    }

    private static int[] getArguments(String[] arguments) {
        int[] result = new int[arguments.length - 1];
        for (int i = 0; i < result.length; i++)
            result[i] = Integer.parseInt(arguments[i + 1]);
        return result;
    }

    //методы выполняющие операции
    private static int add(int... args) throws WrongArgumentException {
        if (args.length < 2) throw new WrongArgumentException("Недостаточно аргуметов");
        int result = 0;
        for (int arg : args) {
            result += arg;
        }
        return result;
    }

    private static int mul(int... args) throws WrongArgumentException {
        if (args.length < 2) throw new WrongArgumentException("Недостаточно аргуметов");
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
    private void writeMessage(String message) {
        System.out.println(message);
    }

    private String readLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public void close() throws IOException {
        reader.close();
        if (writer != null)
            writer.close();
    }

    private static class WrongArgumentException extends Exception {
        public WrongArgumentException(String message) {
            super(message);
        }
    }
}
