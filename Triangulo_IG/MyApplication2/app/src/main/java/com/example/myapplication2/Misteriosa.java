package com.example.myapplication2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
public class Misteriosa {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private ShortBuffer indexBuffer;
    private int numFaces = 6;
    private int colorHandle;
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private int MVPMatrixHandle;
    private int positionHandle;
    private final int program;

    static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private float[] colors = {  // Colors of the 6 faces
            0.0f,  1.0f,  0.0f,  1.0f,
            0.0f,  1.0f,  0.0f,  1.0f,
            1.0f,  0.5f,  0.0f,  1.0f,
            1.0f,  0.5f,  0.0f,  1.0f,
            1.0f,  0.0f,  0.0f,  1.0f,
            1.0f,  0.0f,  0.0f,  1.0f,
            0.0f,  0.0f,  1.0f,  1.0f,
            1.0f,  0.0f,  1.0f,  1.0f
    };
    private float[] colorsL = {  // Colors of the 6 faces
            1.0f,  1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,  1.0f,
            1.0f,  0.5f,  1.0f,  1.0f,
            1.0f,  0.5f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  0.0f,  1.0f,
            0.0f,  0.0f,  1.0f,  1.0f,
            1.0f,  0.0f,  1.0f,  1.0f
    };

    private float[] vertices = {
            // Vertices of the 1 face
            -0.6f, 0.2f, -1.0f,
            -0.4f, -0.5f, -1.0f,
            0.4f,  -0.5f, -1.0f,
            0.6f, 0.2f, -1.0f,
            0.0f, 0.6f,  -1.0f,
            // segunda cara
            -0.6f, 0.2f, 1.0f,
            -0.4f, -0.5f, 1.0f,
            0.4f,  -0.5f, 1.0f,
            0.6f, 0.2f, 1.0f,
            0.0f, 0.6f,  1.0f
    };
    short[] indeces = {
            //cara Frontal (pentagono uno)
            0, 1, 2,
            4, 0, 2,
            3, 4, 2,
            //pared derecha
            2, 8, 3,
            8, 9, 3,
            //Techo derecho
            9, 4, 3,
            5, 4, 9,
            //cara Trasera (pentagono dos)
            9, 5, 8,
            5, 6, 8,
            6, 7, 8,
            //Base
            7, 2, 8,
            7, 1, 2,
            //Pared Izquierda
            7, 0, 1,
            7, 6, 0,
            //Techo Izquierdo
            6, 4, 0,
            6, 5, 4
    };

    // Constructor - Set up the buffers
    public Misteriosa() {
        // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind

        indexBuffer = ByteBuffer.allocateDirect(indeces.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indeces).position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    // Draw the shape
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        MVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mvpMatrix, 0);
        // Render all the faces
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, colors, 0);


        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, indeces.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        //colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        //GLES20.glUniform4fv(colorHandle, 1, colorsL, 0);
        //GLES20.glDrawElements(GLES20.GL_LINE_STRIP, indeces.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
