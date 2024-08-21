import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class BouncingBall_exercicio extends PApplet {


PeasyCam cam;

Vetor posicao,velocidade,aceleracao;
float raio = 40;

public void Bola(float x, float y, float z){
    sphereDetail(12);
    pushMatrix();
      translate(x,y,z);
      sphere(raio);
    popMatrix();
}

public void setup() {
   
  cam = new PeasyCam(this, 1000);
  cam.setMinimumDistance(20);
  cam.setMaximumDistance(2000);
  posicao = new Vetor(100,100,200);
  velocidade = new Vetor(1.4f,1.5f,1.6f);
  aceleracao = new Vetor(0,0,-0.2f);
}

public void draw() {
  background(0);
  desenha_grid();
  velocidade.SomaVetor(aceleracao);
  posicao.SomaVetor(velocidade);
  
  if ((posicao.xcomp > width-raio) || (posicao.xcomp < 0+raio)) {
    velocidade.xcomp = velocidade.xcomp * -1;
  }
  if ((posicao.ycomp > width-raio) || (posicao.ycomp < 0+raio)) {
    velocidade.ycomp = velocidade.ycomp * -1;
  }
  if ((posicao.zcomp > width-raio) || (posicao.zcomp < 0)) {
    velocidade.zcomp = velocidade.zcomp * -0.95f;
    posicao.zcomp = raio;
  }
  
  fill(0,255,0);
  stroke(128);
  Bola(posicao.xcomp, height-posicao.ycomp, posicao.zcomp);
}
///////////////////////////////////////////////////////// classe Vetor

class Vetor {
  float xcomp;
  float ycomp;
  float zcomp;
  
  Vetor(float txcomp, float tycomp, float tzcomp){
    xcomp = txcomp;
    ycomp = tycomp;
    zcomp = tzcomp;
  }

  public void Display(float xpos, float ypos, float zpos, int c){
    stroke(c); 
    line(xpos, ypos, zpos, xpos+xcomp, ypos+ycomp, zpos+zcomp);
  }
  
  public float Magnitude(){
     return sqrt(xcomp*xcomp+ycomp*ycomp+zcomp*zcomp);
  }
  
  public void MultEscalar(float k){
     xcomp = k * xcomp;
     ycomp = k * ycomp;
     zcomp = k * zcomp;
  }
  
  public void SomaVetor(Vetor tVetor){
     xcomp = xcomp + tVetor.xcomp;
     ycomp = ycomp + tVetor.ycomp;
     zcomp = zcomp + tVetor.zcomp;
  }
  
  public void SubtraiVetor(Vetor tVetor){
     xcomp = xcomp - tVetor.xcomp;
     ycomp = ycomp - tVetor.ycomp;
     zcomp = zcomp - tVetor.zcomp;
  }
  
  public float ProdutoEscalar(Vetor tVetor){
     return xcomp * tVetor.xcomp + ycomp * tVetor.ycomp + zcomp * tVetor.zcomp;
  }
  
  public void ProdutoVetorial(Vetor tVetor){
     xcomp = ycomp*tVetor.zcomp - zcomp*tVetor.ycomp;
     ycomp = zcomp*tVetor.xcomp - xcomp*tVetor.zcomp;
     zcomp = xcomp*tVetor.ycomp - ycomp*tVetor.xcomp; 
  }
  
  public boolean Ortogonal(Vetor tVetor){
     if (ProdutoEscalar(tVetor)==0) return true; else return false;
  }
  
  public void Normalizar(){
     xcomp = xcomp / Magnitude();
     ycomp = ycomp / Magnitude();
     zcomp = zcomp / Magnitude();
  }
}

//////////////////////////// funções isoladas

public Vetor ProdutoVetorial(Vetor v1, Vetor v2) {
     float t1 = v1.ycomp*v2.zcomp;
     float t2 = v1.zcomp*v2.xcomp;
     float t3 = v1.xcomp*v2.ycomp;
     float t4 = v1.ycomp*v2.xcomp;
     float t5 = v1.zcomp*v2.ycomp;
     float t6 = v1.xcomp*v2.zcomp;
     Vetor v3;
     v3 = new Vetor(t1-t5, t2-t6, t3-t4);
     return v3;
}  

public float Magnitude(Vetor v) {
     return sqrt(v.xcomp*v.xcomp+v.ycomp*v.ycomp+v.zcomp*v.zcomp);   
}
// normaliza e multiplica por escalar
public void NormalizaMultK(Vetor v, float k){
     v.xcomp = k * v.xcomp / Magnitude(v);
     v.ycomp = k * v.ycomp / Magnitude(v);
     v.zcomp = k * v.zcomp / Magnitude(v);
}

public Vetor Soma2Vetores(Vetor v1, Vetor v2){
  Vetor v3;
  v3 = new Vetor(v1.xcomp+v2.xcomp, v1.ycomp+v2.ycomp, v1.zcomp+v2.zcomp);
  return v3;
}

public Vetor Subtrai2Vetores(Vetor v1, Vetor v2){
  Vetor v3;
  v3 = new Vetor(v1.xcomp-v2.xcomp, v1.ycomp-v2.ycomp, v1.zcomp-v2.zcomp);
  return v3;
}
public void desenha_grid(){
  stroke(50);
  background(0);
  // grid
  for(int i=0; i<=height; i=i+50){
    line(0,i,width,i);
    line(i,0,i,height);
  }
  pushMatrix();
    // marcadores
    fill(255,255,255);
    box(10); text("  origem(0,0)", 0, height);
    translate(500,0,0);
    box(10); text("  X MAX("+width+",0)",  0, width);
    translate(0,500,0);
    box(10);
    translate(-500,0,0);
    box(10); text("  Y MAX(0,"+height+")", 0, -height); 
  popMatrix();
  pushMatrix();
    // marcadores z=500
    fill(255,255,0);
    translate(0,0,500);
    box(10); 
    translate(500,0,0);
    box(10);
    translate(0,500,0);
    box(10);
    translate(-500,0,0);
    box(10); 
  popMatrix();
}
  public void settings() {  size(500,500,P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "BouncingBall_exercicio" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
