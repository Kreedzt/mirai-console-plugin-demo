package org.example.mirai.plugin

import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.example.kreedzt-plugin",
        name = "TestPlugin-v0",
        version = "0.1.1"
    ) {
        author("Kreedzt")

        info("""
            Kreedzt:test
        """.trimIndent())
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }

        MySimpleCommand.register()
        MyCompositeCommand.register()

        this.globalEventChannel().subscribeAlways<MessageEvent>{event ->
            logger.info("eee")
            logger.info(event.toString())
            logger.info(event.message.toString())
            this.toCommandSender().sendMessage("Test Res")
        }

        this.globalEventChannel().subscribeAlways<FriendMessageEvent>{event ->
            logger.info("This is friendMessage: ${event.senderName}")
            event.toCommandSender().sendMessage("朋友消息")
        }
    }

    override fun onDisable() {
        MySimpleCommand.unregister()
        MyCompositeCommand.unregister()
    }
}

// 简单指令
object MySimpleCommand: SimpleCommand(
    PluginMain,
    "foo",
    "私聊",
    description = "示例指令",
) {
    @Handler
    suspend fun CommandSender.handle(int: Int, str: String) {
        sendMessage("/foo 的第一个参数是 $int, 第二个是 $str")
    }
}

object MyCompositeCommand: CompositeCommand(
    PluginMain, "manage",
    description = "示例复杂指令"
) {
    @SubCommand
    suspend fun CommandSender.mute(target: Member, duration: Int) {
        sendMessage("/manage mute 被调用了, 参数为; $target, $duration")
    }

    @SubCommand
    suspend fun CommandSender.list() {
        sendMessage("/manage list 被调用了")
    }

    @SubCommand
    suspend fun CommandSender.test(image: Image) {
        sendMessage("/manage image 被调用了, 图片是 ${image.imageId}")
    }
}