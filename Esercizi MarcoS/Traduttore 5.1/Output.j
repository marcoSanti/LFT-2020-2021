.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 20
L0:
 istore 0
L1:
 iload 0
L3:
L3:
 goto L0
 invokestatic Output/print(I)V
L2:
 invokestatic Output/read()I
 istore 1
L4:
 iload 1
L9:
L9:
 ldc 10
L10:
 if_icmplt L8
 goto L7
L8:
 ldc 100
L12:
L12:
 goto L7
 invokestatic Output/print(I)V
L11:
 goto L7
 goto L6
L7:
 iload 1
L15:
L15:
 ldc 10
L16:
 if_icmpgt L14
 goto L0
L14:
 ldc 200
L18:
L18:
 goto L0
 invokestatic Output/print(I)V
L17:
 goto L0
 goto L6
L13:
 iload 1
L21:
L21:
 ldc 10
L22:
 if_icmpeq L20
 goto L0
L20:
 iload 1
L24:
L24:
 goto L0
 invokestatic Output/print(I)V
L23:
 goto L0
 goto L6
L19:
 goto L0
 ldc 1
L26:
L26:
 goto L0
 invokestatic Output/print(I)V
L25:
 goto L0
L6:
L5:
L29:
 iload 1
L30:
L30:
 ldc 0
L31:
 if_icmpge L0
 goto L28
L28:
 iload 1
L33:
L33:
 goto L0
 invokestatic Output/print(I)V
L32:
 iload 1
L0:
 ldc 1
L0:
 isub 
L0:
 istore 1
L34:
 goto L0
 goto L29
L27:
 goto L0
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

