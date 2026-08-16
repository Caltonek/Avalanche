package pl.caltonek.avalanche.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.execution.ScriptExecutionManager;
import pl.caltonek.avalanche.execution.script.ScriptExecutor;

@Environment(EnvType.CLIENT)
public final class AvalancheCommand {

    private AvalancheCommand() {
    }

    public static void register(@NotNull final ScriptExecutionManager executionManager) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("avalanche")
                        .then(ClientCommandManager.literal("execute")
                                .then(ClientCommandManager.argument("script", StringArgumentType.greedyString())
                                        .suggests((context, builder) -> CommandSource.suggestMatching(
                                                executionManager.getAvailableScripts(),
                                                builder
                                        ))
                                        .executes(context -> {
                                            final String script = StringArgumentType.getString(context, "script");

                                            try {
                                                executionManager.execute(script);
                                                context.getSource().sendFeedback(Text.literal("Started script: " + script));
                                                return 1;
                                            } catch (final RuntimeException exception) {
                                                context.getSource().sendFeedback(Text.literal("§c" + exception.getMessage()));
                                                return 0;
                                            }
                                        })
                                )
                        )
                        .then(ClientCommandManager.literal("halt")
                                .then(ClientCommandManager.argument("script", StringArgumentType.greedyString())
                                        .suggests((context, builder) -> CommandSource.suggestMatching(
                                                executionManager.getActiveScripts().stream().map(ScriptExecutor::getName),
                                                builder
                                        ))
                                        .executes(context -> {
                                            final String script = StringArgumentType.getString(context, "script");

                                            if (!executionManager.isRunning(script)) {
                                                context.getSource().sendFeedback(Text.literal("§cScript is not running: " + script));
                                                return 0;
                                            }

                                            executionManager.halt(script);
                                            context.getSource().sendFeedback(Text.literal("Halted script: " + script));
                                            return 1;
                                        })
                                )
                        )
                        .then(ClientCommandManager.literal("list")
                                .executes(context -> {
                                    final var activeScripts = executionManager.getActiveScripts();

                                    if (activeScripts.isEmpty()) {
                                        context.getSource().sendFeedback(Text.literal("No scripts are currently running."));
                                        return 1;
                                    }

                                    context.getSource().sendFeedback(Text.literal("Running scripts:"));
                                    activeScripts.forEach(script -> context.getSource().sendFeedback(
                                            Text.literal(" - " + script.getName() + " [" + script.getState().getDisplayName() + "]")
                                    ));
                                    return 1;
                                })
                        )
                )
        );
    }
}