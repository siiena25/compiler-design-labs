import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Position {
    private int line, pos;

    Position(int line, int pos) {
        this.line = line;
        this.pos = pos;
    }

    void nextLine() {
        this.line += 1;
        this.pos = 1;
    }

    void nextPos(int pos) {
        this.pos += pos;
    }

    public String toString() {
        return "(" + this.line + ", " + this.pos + ")";
    }
}


public class Main {
    private static void test_match(String line, Position pos) {
        String fibonacciNumber = "0*(10+)*1?(?<=.)(?=$|[^01])";
        String ident = "(^[aeiouy]?([qwrtplkjhgfdszxcvbnm][aeiouy])+[qwrtplkjhgfdszxcvbnm]?)" +
                "|(^[qwrtplkjhgfdszxcvbnm]?([aeiouy][qwrtplkjhgfdszxcvbnm])+[aeiouy]?)";
        String pattern = "(?<ident>^"+ident+")|(?<fibonacciNumber>^"+fibonacciNumber+")";

        Pattern p = Pattern.compile(pattern);
        Matcher m;

        while (!line.equals("")) {
            m = p.matcher(line);
            if (m.find()) {
                String item;
                if (m.group("fibonacciNumber") != null) {
                    item = m.group("fibonacciNumber");
                    System.out.println("FIB " + pos.toString() + ": " + item);
                } else {
                    item = m.group("ident");
                    System.out.println("IDENT " + pos.toString() + ": " + item);
                }
                pos.nextPos(item.length());
                line = line.substring(line.indexOf(item) + item.length());
            }
            else {
                if (Character.isWhitespace(line.charAt(0))) {
                    while (Character.isWhitespace(line.charAt(0))) {
                        line = line.substring(1);
                        pos.nextPos(1);
                    }
                }
                else {
                    while (!m.find() && !line.equals("")) {
                        int firstWhitespaceIndex = -1;
                        for (int i = 0; i < line.length(); i++) {
                            if (line.charAt(i) == ' ') {
                                firstWhitespaceIndex = i;
                                break;
                            }
                        }
                        System.out.print("syntax error " + pos + ": ");
                        if (firstWhitespaceIndex != -1) {
                            System.out.println(line.substring(0, firstWhitespaceIndex));
                            line = line.substring(firstWhitespaceIndex + 1);
                            pos.nextPos(firstWhitespaceIndex + 1);
                        } else {
                            System.out.println(line.substring(1));
                            line = line.substring(1);
                            pos.nextPos(1);
                        }
                        m = p.matcher(line);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Position pos = new Position(1,1);
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("test.txt"), StandardCharsets.UTF_8);
            for (String line : lines) {
                test_match(line,pos);
                pos.nextLine();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}