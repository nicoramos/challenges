import Entities.FileMemory;
import Exceptions.ExitException;
import Utils.CommandExecution;
import Utils.FileType;

import java.util.Arrays;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileMemory fileMemory = new FileMemory(null, ".", FileType.DIRECTORY);

        try {
            while (true) {
                try {
                    String inputs = scanner.nextLine();
                    String[] input = Arrays.copyOf(inputs.split(" "), 2);

                    fileMemory = CommandExecution.getCommand(input[0]).apply(fileMemory, input[1]);
                    System.out.println(fileMemory.getMessage());

                    fileMemory.setMessage(null);
                } catch (ExitException ee) {
                    throw ee;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (ExitException e) {
            System.out.println("Finished...");
        }
    }
}
