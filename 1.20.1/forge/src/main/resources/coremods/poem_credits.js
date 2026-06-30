var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var Opcodes = Java.type('org.objectweb.asm.Opcodes')
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode')
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode')
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')

var T_IMODINFO = "Lnet/minecraftforge/forgespi/language/IModInfo;"

function initializeCoreMod() {
    return {
        'poem_credits': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.client.gui.ModListScreen',
                'methodName': 'updateCache',
                'methodDesc': '()V',
            },
            'transformer': function (methodNode) {
                var lvIndex = methodNode.localVariables.stream()
                    .filter(function (lv) { return T_IMODINFO === lv.desc })
                    .mapToInt(function (lv) { return lv.index })
                    .findFirst()
                    .orElse(-1)

                if (lvIndex < 0) {
                    ASMAPI.log("ERROR", "Failed to find local variable with type {}", T_IMODINFO);
                    return;
                }

                var call = ASMAPI.findFirstMethodCall(
                    methodNode, ASMAPI.MethodType.VIRTUAL,
                    "net/minecraftforge/client/gui/ModListScreen$InfoPanel",
                    "setInfo",
                    "(Ljava/util/List;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraftforge/common/util/Size2i;)V"
                );
                // (List, ResourceLocation, Size2i) -> void

                var instructions = ASMAPI.listOf(
                    // A B C (top)
                    new InsnNode(Opcodes.DUP_X2),
                    new InsnNode(Opcodes.POP),
                    // C A B (top)
                    new InsnNode(Opcodes.DUP_X2),
                    new InsnNode(Opcodes.POP),
                    // B C A (top)
                    new InsnNode(Opcodes.DUP_X2),
                    // A B C A (top)

                    new VarInsnNode(Opcodes.ALOAD, lvIndex),
                    new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "xland/mcmod/epx/v4/forge/PoemCreditsForge",
                        "appendLinesForge",
                        "(Ljava/util/List;" + T_IMODINFO + ")V"
                    )
                );

                ASMAPI.insertInsnList(
                    methodNode, call, instructions,
                    ASMAPI.InsertMode.INSERT_BEFORE
                );

                return methodNode
            },
        }
    }
}