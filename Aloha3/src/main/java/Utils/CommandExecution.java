package Utils;

import Entities.FileMemory;

import Exceptions.ExitException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import java.util.function.Predicate;

public enum CommandExecution {

    QUIT("quit"::equalsIgnoreCase) {
        @Override
        protected BiFunction<FileMemory, String, FileMemory> command() {
            return (fileMemory, parameter) -> { throw new ExitException(); };
        }
    },

    CURRENT_DIRECTORY("pwd"::equalsIgnoreCase) {
        @Override
        protected BiFunction<FileMemory, String, FileMemory> command() {
            return (fileMemory, parameter) -> {
                List<String> path = new ArrayList<>();
                if (fileMemory.getFileType() == FileType.DIRECTORY) {
                    path.add(fileMemory.getName());
                }

                FileMemory auxFather = fileMemory.getFather();
                while (auxFather != null) {
                    path.add(auxFather.getName());
                    auxFather = auxFather.getFather();
                }

                StringBuilder str = new StringBuilder();
                for (int i = path.size() - 1; i >= 0; i--) {
                    if (i == path.size() - 1) {
                        str.append(path.get(i));
                    } else {
                        str.append(String.format("/%s", path.get(i)));
                    }
                }

                return fileMemory.setMessage(str.toString());
            };
        }
    },
    LIST_CONTENTS("ls"::equalsIgnoreCase) {
        @Override
        protected BiFunction<FileMemory, String, FileMemory> command() {
            return (file, parameter) -> file.setMessage(getLs(file, "-r".equals(parameter), new StringBuilder("- NAME\t\tTYPE\n"), new StringBuilder()));
        }

        private String getLs(FileMemory file, boolean recursive, StringBuilder str, StringBuilder prev) {
            if (file.getChilds().size() == 0)
                return "";

            file.getChilds()
                .forEach(f -> {
                    str.append(String.format("%s %s\t\t%s\n", prev.toString(), f.getName(), f.getFileType().name()));
                    if (recursive) {
                        prev.append("- ");
                        String ls = getLs(f, true, new StringBuilder(), prev);
                        if (!ls.equals("")) {
                            str.append(String.format("%s %s", prev, ls));
                        }
                    }
                });

            return str.toString();
        }
    },
    MAKE_DIRECTORY("mkdir"::equalsIgnoreCase) {
        @Override
        protected BiFunction<FileMemory, String, FileMemory> command() {
            return (file, parameter) -> {
                if (parameter == null || "".equals(parameter)) {
                    return file.setMessage("Parameter not defined");
                }

                if (parameter.length() > 100) {
                    return file.setMessage("File name too long");
                }

                file.addChild(new FileMemory(file, parameter, FileType.DIRECTORY));
                return file.setMessage(String.format("Directory %s created.", parameter));
            };
        }
    },
    CREATE_FILE ("touch"::equalsIgnoreCase) {
        @Override
        protected BiFunction<FileMemory, String, FileMemory> command() {
            return (file, parameter) -> {
                if (parameter == null || "".equals(parameter)) {
                    return file.setMessage("Directory not found");
                }

                if (parameter.length() > 100) {
                    return file.setMessage("File name too long");
                }

                file.addChild(new FileMemory(file, parameter, FileType.FILE));
                return file.setMessage(String.format("File %s created.", parameter));
            };
        }
    },
    CHANGE_DIRECTORY("cd"::equalsIgnoreCase) {
        @Override
        protected BiFunction<FileMemory, String, FileMemory> command() {
            return (file, parameter) -> {
                if (parameter == null || "".equals(parameter)) {
                    return file.setMessage("Invalid path");
                }

                FileMemory fileAux;
                if (parameter.equals("..")) {
                    fileAux = file.getFather();
                } else {
                    fileAux = file.getChilds()
                                  .stream()
                                  .filter(c -> parameter.equals(c.getName()))
                                  .findFirst()
                                  .orElse(null);
                }

                if (fileAux == null) {
                    return file.setMessage("Invalid path");
                }

                if (fileAux.getFileType() == FileType.FILE) {
                    return file.setMessage(String.format("%s is not a directory", fileAux.getName()));
                }

                return fileAux.setMessage(String.format("> %s", fileAux.getName()));
            };
        }
    },
    DEFAULT((input) -> true) {
        @Override
        protected BiFunction<FileMemory, String, FileMemory> command() {
            return (file, parameter) -> file.setMessage("Command not found.");
        }
    };

    private final Predicate<String> predicate;
    CommandExecution(Predicate<String> predicate) {
        this.predicate = predicate;
    }

    protected abstract BiFunction<FileMemory, String, FileMemory> command();

    public static BiFunction<FileMemory, String, FileMemory> getCommand(String input) {
        return Arrays.stream(values())
                     .filter(ce -> ce.predicate.test(input))
                     .findFirst()
                     .orElse(DEFAULT)
                     .command();
    }
}
