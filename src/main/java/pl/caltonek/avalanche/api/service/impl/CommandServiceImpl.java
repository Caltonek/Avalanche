package pl.caltonek.avalanche.api.service.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import pl.caltonek.avalanche.api.service.CommandService;
import pl.caltonek.avalanche.execution.script.ScriptContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CommandServiceImpl implements CommandService {

    private CommandDispatcher<FabricClientCommandSource> dispatcher;
    private final Map<String, RegisteredLuaCommand> registeredCommands = new ConcurrentHashMap<>();

    private record RegisteredLuaCommand(
            String scriptName,
            String commandName,
            LuaValue executor,
            LuaValue tabCompleter
    ) {}

    public void setDispatcher(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public boolean isRegistered(@NotNull String name) {
        final String cleanName = (name.startsWith("/") ? name.substring(1) : name).toLowerCase();
        return registeredCommands.containsKey(cleanName);
    }

    @Override
    public boolean IsRegistered(@NotNull String name) {
        return isRegistered(name);
    }

    @Override
    public void register(@NotNull String name, @NotNull LuaValue executor) {
        register(name, executor, LuaValue.NIL);
    }

    @Override
    public void Register(@NotNull String name, @NotNull LuaValue executor) {
        register(name, executor);
    }

    @Override
    public void Register(@NotNull String name, @NotNull LuaValue executor, @NotNull LuaValue tabCompleter) {
        register(name, executor, tabCompleter);
    }

    @Override
    public void register(@NotNull String name, @NotNull LuaValue executor, @NotNull LuaValue tabCompleter) {
        final String cleanName = (name.startsWith("/") ? name.substring(1) : name).toLowerCase();
        final String currentScript = ScriptContext.getCurrentScript() != null ? ScriptContext.getCurrentScript() : "global";

        unregister(cleanName);

        if (dispatcher == null) {
            dispatcher = ClientCommandManager.getActiveDispatcher();
        }

        var commandNodeBuilder = ClientCommandManager.literal(cleanName)
                .executes(context -> {
                    executeCommand(cleanName, "");
                    return 1;
                })
                .then(ClientCommandManager.argument("args", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            LuaValue result = LuaValue.NIL;
                            if (tabCompleter.isfunction()) {
                                try {
                                    result = tabCompleter.call(LuaValue.valueOf(builder.getRemaining()));
                                } catch (Exception ignored) {}
                            } else if (tabCompleter.istable()) {
                                result = tabCompleter;
                            }

                            if (result.istable()) {
                                LuaTable table = result.checktable();
                                for (int i = 1; i <= table.length(); i++) {
                                    LuaValue val = table.get(i);
                                    if (val.isstring()) {
                                        builder.suggest(val.tojstring());
                                    }
                                }
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            final String args = StringArgumentType.getString(context, "args");
                            executeCommand(cleanName, args);
                            return 1;
                        })
                );

        if (dispatcher != null) {
            dispatcher.register(commandNodeBuilder);
        }

        var client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null && client.getNetworkHandler().getCommandDispatcher() != null) {
            try {
                @SuppressWarnings("unchecked")
                var root = (CommandNode<Object>) (Object) client.getNetworkHandler().getCommandDispatcher().getRoot();
                @SuppressWarnings("unchecked")
                var node = (CommandNode<Object>) (Object) commandNodeBuilder.build();

                root.addChild(node);
            } catch (Exception ignored) {}
        }

        registeredCommands.put(cleanName, new RegisteredLuaCommand(currentScript, cleanName, executor, tabCompleter));
    }

    @Override
    public void unregister(@NotNull String name) {
        final String cleanName = (name.startsWith("/") ? name.substring(1) : name).toLowerCase();
        final RegisteredLuaCommand cmd = registeredCommands.remove(cleanName);

        if (cmd != null) {
            if (dispatcher != null) {
                removeNodeFromRoot(dispatcher.getRoot(), cleanName);
            }
            var client = MinecraftClient.getInstance();
            if (client.getNetworkHandler() != null && client.getNetworkHandler().getCommandDispatcher() != null) {
                removeNodeFromRoot(client.getNetworkHandler().getCommandDispatcher().getRoot(), cleanName);
            }
        }
    }

    @Override
    public void Unregister(@NotNull String name) {
        unregister(name);
    }

    private void removeNodeFromRoot(CommandNode<?> root, String name) {
        if (root == null) return;
        try {
            Field childrenField = CommandNode.class.getDeclaredField("children");
            childrenField.setAccessible(true);
            Map<?, ?> children = (Map<?, ?>) childrenField.get(root);
            children.remove(name);

            Field literalsField = CommandNode.class.getDeclaredField("literals");
            literalsField.setAccessible(true);
            Map<?, ?> literals = (Map<?, ?>) literalsField.get(root);
            literals.remove(name);

            Field argumentsField = CommandNode.class.getDeclaredField("arguments");
            argumentsField.setAccessible(true);
            Map<?, ?> arguments = (Map<?, ?>) argumentsField.get(root);
            arguments.remove(name);
        } catch (Exception ignored) {}
    }

    public void unregisterListeners(@NotNull String scriptName) {
        List<String> toRemove = new ArrayList<>();
        registeredCommands.forEach((name, cmd) -> {
            if (cmd.scriptName().equals(scriptName)) {
                toRemove.add(name);
            }
        });
        toRemove.forEach(this::unregister);
    }

    public void clearAllListeners() {
        new ArrayList<>(registeredCommands.keySet()).forEach(this::unregister);
    }

    public void executeCommand(String commandName, String args) {
        final RegisteredLuaCommand cmd = registeredCommands.get(commandName.toLowerCase());
        if (cmd != null && cmd.executor().isfunction()) {
            try {
                cmd.executor().call(LuaValue.valueOf(args));
            } catch (Exception e) {
                System.err.println("[Exec Error] Error while executing the command /" + commandName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}