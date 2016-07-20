package pl.pollub.hirols.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.ArrayList;

/**
 * Created by erykp_000 on 2016-07-15.
 */
public abstract class DefaultConsole implements Console {

    protected final Logger logger;
    protected CommandsContainer commands;

    protected boolean hiddenCommandsExecute = true;
    protected boolean hiddenCommandVisibility = false;

    protected static final LogLevel DEFAULT_LOG_LEVEL = new LogLevel(new Color(1,1,1,1), ">");
    protected static final LogLevel ERROR_LOG_LEVEL = new LogLevel(new Color(1,0,0,1), "Error: ");
    protected static final LogLevel COMMAND_LOG_LEVEL = new LogLevel(new Color(0,0,1,1), "] ");

    public DefaultConsole(CommandsContainer commandsContainer) {
        this.logger = new Logger();
        this.commands = commandsContainer;
        commandsContainer.setConsole(this);
    }

    @Override
    public void executeCommand(String command) {
        log(command,COMMAND_LOG_LEVEL);

        String[] commandSplit = command.split(" ");
        String methodName = commandSplit[0];
        int argumentsLength = (commandSplit.length > 1) ? commandSplit.length - 1 : 0;
        String[] arguments = new String[argumentsLength];
        System.arraycopy(commandSplit, 1, arguments, 0, argumentsLength);

        Class<? extends CommandsContainer> commandsContainerClazz = commands.getClass();
        Method[] methods = ClassReflection.getMethods(commandsContainerClazz);
        ArrayList<Method> methodsToExecute = new ArrayList<Method>();
        for(Method method : methods) {
            if(method.getName().equalsIgnoreCase(methodName) && !(method.isAnnotationPresent(Hidden.class) && !isHiddenCommandsExecute())) {
                methodsToExecute.add(method);
            }
        }

        if(methodsToExecute.size() < 1) {
            log(methodName + " <-- there is no such command, type showcommands to print them all", ERROR_LOG_LEVEL);
            return;
        }

        for(Method method : methodsToExecute) {
            Class[] parameterTypes = method.getParameterTypes();
            if(parameterTypes.length != arguments.length) continue;
            if(parameterTypes.length == 0) {
                try {
                    method.setAccessible(true);
                    method.invoke(commands,null);
                    return;
                } catch (ReflectionException e) {
                    e.printStackTrace();
                    return;
                }
            }
            Object[] methodArguments = new Object[parameterTypes.length];
            int i = -1;
            try {
                for (Class parameter : parameterTypes) {
                    i++;
                    if (parameter.equals(String.class)) {
                        methodArguments[i] = arguments[i];
                    } else if (parameter.equals(Float.class) || parameter.equals(float.class)) {
                        methodArguments[i] = Float.parseFloat(arguments[i]);
                    } else if (parameter.equals(Double.class) || parameter.equals(double.class)) {
                        methodArguments[i] = Double.parseDouble(arguments[i]);
                    }  else if (parameter.equals(Integer.class) || parameter.equals(int.class)) {
                        methodArguments[i] = Integer.parseInt(arguments[i]);
                    } else if (parameter.equals(Long.class) || parameter.equals(long.class)) {
                        methodArguments[i] = Long.parseLong(arguments[i]);
                    } else if (parameter.equals(Short.class) || parameter.equals(short.class)) {
                        methodArguments[i] = Short.parseShort(arguments[i]);
                    } else if (parameter.equals(Byte.class) || parameter.equals(byte.class)) {
                        methodArguments[i] = Byte.parseByte(arguments[i]);
                    }
                }
            } catch (Exception e) {
                continue;
            }

            try {
                method.setAccessible(true);
                method.invoke(commands, methodArguments);
                return;
            } catch (ReflectionException e) {
                e.printStackTrace();
                return;
            }

        }
        log("bad parameters", ERROR_LOG_LEVEL);
    }

    @Override
    public void showCommands() {
        Class<? extends CommandsContainer> commandsContainerClazz = commands.getClass();
        Method[] methods = ClassReflection.getDeclaredMethods(commandsContainerClazz);
        for(Method method : methods) {
            if(!(method.isAnnotationPresent(Hidden.class) && !isHiddenCommandsVisible())) {
                String command = method.getName() + ": ";
                Class[] parameters = method.getParameterTypes();
                for(Class parameter : parameters) {
                    command += parameter.getSimpleName() + "; ";
                }
                log(command);
            }
        }
    }

    @Override
    public void saveLogsToFile(String file) {
        FileHandle fileHandle = Gdx.files.local(file);
        if(fileHandle.isDirectory()) throw new IllegalArgumentException("not a file!");

        fileHandle.writeString(logger.getLogsInOneString(),false);
    }

    @Override
    public void log(String message) {
        logger.log(message,DEFAULT_LOG_LEVEL);
    }

    @Override
    public void log(String message, LogLevel logLevel) {
        logger.log(message, logLevel);
    }

    @Override
    public boolean isHiddenCommandsVisible() {
        return hiddenCommandVisibility;
    }

    @Override
    public boolean isHiddenCommandsExecute() {
        return hiddenCommandsExecute;
    }

    @Override
    public void setHiddenCommandsVisibility(boolean hiddenCommandsVisibility) {
        this.hiddenCommandVisibility = hiddenCommandsVisibility;
    }

    @Override
    public void setHiddenCommandsExecute(boolean hiddenCommandsExecute) {
        this.hiddenCommandsExecute = hiddenCommandsExecute;
    }

    @Override
    public void setCommands(CommandsContainer commands) {
        this.commands = commands;
    }
}
