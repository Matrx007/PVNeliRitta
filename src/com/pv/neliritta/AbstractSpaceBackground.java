package com.pv.neliritta;

import com.pv.neliritta.gui.Action;
import com.pv.neliritta.gui.Color;
import processing.core.PConstants;
import processing.core.PImage;

public class AbstractSpaceBackground {
    private static class Particle {
        float rotationX, rotationY, rotationZ;
        float rotationSpeedX, rotationSpeedY, rotationSpeedZ;
        float distance;

        PImage particle;

        Color randomColor;

        public Particle(float rotationX, float rotationY, float rotationZ, float distance) {
            this.rotationX = rotationX;
            this.rotationY = rotationY;
            this.rotationZ = rotationZ;
            this.distance = distance;

            randomColor = new Color(
                    160 + (int)(Math.random() * 40 - 20),
                    160 + (int)(Math.random() * 40 - 20),
                    160 + (int)(Math.random() * 40 - 20),
                    255
            );

            this.rotationSpeedX = (float)Math.random() * 0.1f;
            this.rotationSpeedY = (float)Math.random() * 0.1f;
            this.rotationSpeedZ = (float)Math.random() * 0.1f;
        }
    }





    private Main main;

    private Particle[] particles;

    private PImage particleImage;

    private float smoothMouseX = 0, smoothMouseY = 0;

    private float rotationX = 0, rotationY = 0, rotationZ = 0;
    private float cameraRotationX = 0, cameraRotationY = 0;

    public AbstractSpaceBackground(Main main) {
        this.main = main;
        particles = new Particle[200];

        for(int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(
                    (float)(Math.random() * Math.PI * 2f),
                    (float)(Math.random() * Math.PI * 2f),
                    (float)(Math.random() * Math.PI * 2f),
                    (float)(Math.random() * 2048f + 2048f)
            );
        }

        particleImage = GraphicsManager.loadedGraphics.get("particle-circle");
    }

    public void resize() {

    }

    public void update(double deltaTime) {
        float shortestDimension = Math.min(
                main.getGame().pixelWidth,
                main.getGame().pixelHeight
        );

        float mappedMouseX = main.getGame().mouseX - main.getGame().pixelWidth  / 2f;
        float mappedMouseY = main.getGame().mouseY - main.getGame().pixelHeight / 2f;

        final float padding = 80f;
        mappedMouseX = Math.max(Math.min(mappedMouseX, main.guiSize/2f - padding), -main.guiSize/2f + padding);
        mappedMouseY = Math.max(Math.min(mappedMouseY, main.guiSize/2f - padding), -main.guiSize/2f + padding);

        smoothMouseX += (mappedMouseX - smoothMouseX) * deltaTime * 10f;
        smoothMouseY += (mappedMouseY - smoothMouseY) * deltaTime * 10f;

        cameraRotationX = -smoothMouseY  / shortestDimension * 2f / 7f;
        cameraRotationY = smoothMouseX / shortestDimension * 2f / 7f;

//        cameraRotationX = -centeredMouseY / shortestDimension * 2f;
//        cameraRotationY = centeredMouseX / shortestDimension * 2f;

        /*rotationX += deltaTime * 0.1f;
        rotationY += deltaTime * 0.1f;
        rotationZ += deltaTime * 0.1f;*/


        for(int i = 0; i < particles.length; i++) {
            particles[i].rotationX += particles[i].rotationSpeedX * deltaTime;
            particles[i].rotationY += particles[i].rotationSpeedY * deltaTime;
            particles[i].rotationZ += particles[i].rotationSpeedZ * deltaTime;
        }
    }

    public void render(Action renderGUI) {
        main.getGame().perspective(
                (float)Math.PI/3f,
                (float)main.getGame().pixelWidth/main.getGame().pixelHeight,
                1,100000);

        main.getGame().blendMode(PConstants.ADD);

        main.getGame().rectMode(PConstants.CENTER);
        main.getGame().imageMode(PConstants.CENTER);

        main.getGame().hint(PConstants.DISABLE_DEPTH_TEST);

//        PMatrix original = main.getGame().getMatrix();
        main.getGame().pushMatrix();

        main.getGame().translate(
                main.getGame().pixelWidth / 2f,
                main.getGame().pixelHeight / 2f
        );

        main.getGame().scale(3f);

        main.getGame().rotateX(cameraRotationX);
        main.getGame().rotateY(cameraRotationY);

        main.getGame().rotateX(rotationX);
        main.getGame().rotateY(rotationY);
        main.getGame().rotateZ(rotationZ);

//        main.getGame().translate(0, 100, 0);

        for(int i = 0; i < particles.length; i++) {
            main.getGame().pushMatrix();
            main.getGame().rotateX(particles[i].rotationX);
            main.getGame().rotateY(particles[i].rotationY);
            main.getGame().rotateZ(particles[i].rotationZ);
            main.getGame().translate(0, 0, particles[i].distance);

//            main.getGame().fill(255, 255, 255, 64);
//            main.getGame().rect(-100, -100, 200, 200);
            main.getGame().tint(
                    particles[i].randomColor.r,
                    particles[i].randomColor.g,
                    particles[i].randomColor.b,
                    16
            );
            main.getGame().scale(9f);
            main.getGame().image(particleImage, 0, 0);
            main.getGame().noTint();
            /*main.getGame().fill(
                    particles[i].randomColor.r,
                    particles[i].randomColor.g,
                    particles[i].randomColor.b,
                    255);
            main.getGame().circle(0, 0, 128f);*/

            main.getGame().popMatrix();

//            main.getGame().setMatrix(original);
        }


        main.getGame().rectMode(PConstants.CORNER);
        main.getGame().imageMode(PConstants.CORNER);

        main.getGame().translate(
                -main.getGame().pixelWidth / 2f,
                -main.getGame().pixelHeight / 2f
        );

        float guiOffsetZ = -main.guiSize;
        main.getGame().translate(0, 0, guiOffsetZ);
        main.getGame().pushStyle();
        main.getGame().pushMatrix();
        main.getGame().blendMode(PConstants.NORMAL);
        renderGUI.run();

        main.getGame().popMatrix();
        main.getGame().popStyle();
        main.getGame().translate(0, 0, -guiOffsetZ);


        main.getGame().rectMode(PConstants.CORNER);
        main.getGame().imageMode(PConstants.CORNER);

        main.getGame().hint(PConstants.ENABLE_DEPTH_TEST);

        main.getGame().blendMode(PConstants.NORMAL);


        main.getGame().translate(
                main.getGame().pixelWidth / 2f,
                main.getGame().pixelHeight / 2f
        );

        main.getGame().imageMode(PConstants.CENTER);
        main.getGame().noTint();
        main.getGame().translate(0, 0, guiOffsetZ+main.guiSize*0.6f);
        main.getGame().image(GraphicsManager.loadedGraphics.get("border"), 0, 0,
                main.getGame().pixelWidth*1.5f, main.getGame().pixelHeight*1.5f);
        main.getGame().translate(0, 0, -(guiOffsetZ+main.guiSize*0.6f));

        main.getGame().popMatrix();
        main.getGame().imageMode(PConstants.CORNER);
        main.getGame().image(GraphicsManager.loadedGraphics.get("vignette"), 0, 0,
                main.getGame().pixelWidth, main.getGame().pixelHeight);

    }
}
