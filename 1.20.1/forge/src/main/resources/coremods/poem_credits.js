var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var Opcodes = Java.type('org.objectweb.asm.Opcodes')
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode')
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')

function initializeCoreMod() {
    return {
        'poem_credits': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraftforge.client.gui.ModListScreen$InfoPanel',
                'methodName': 'resizeContent',
                'methodDesc': '(Ljava/util/List;)Ljava/util/List;',
            },
            'transformer': function (methodNode) {
                var instructions = ASMAPI.listOf(
                    new VarInsnNode(Opcodes.ALOAD, 1),
                    new MethodInsnNode(Opcodes.INVOKESTATIC, 'xland/mcmod/epx/v4/forge/PoemCreditsForge', 'appendLines', '(Ljava/util/List;)V'),
                )
                ASMAPI.insertInsnList(methodNode, 0, instructions, ASMAPI.InsertMode.INSERT_BEFORE)
            },
        }
    }
}