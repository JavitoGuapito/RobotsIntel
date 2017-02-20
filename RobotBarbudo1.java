package jop;

import robocode.AdvancedRobot;
import robocode.Bullet;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.control.events.BattleStartedEvent;

import java.awt.Color;

import javax.xml.stream.events.EndElement;

/**
* A - a robot by Javier Ortiz Pérez
*/

public class RobotBarbudo1 extends Robot
{
	private Double enemyBearing;
	private Integer contadorAciertosDirecto = 0;
	private Integer contadorAciertosRight = 0;
	private Integer contadorAciertosLeft = 0;
	private Integer contadorDisparos= 0;
	private Integer modoDisparo = 0;
	private Boolean elegir;
	private Integer contadorFallos = 0;
	
	public void run() {
		elegir = true;
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		
		setColors(Color.pink,Color.black,Color.pink); // body,gun,radar
		while(true) {
			turnRadarLeft(25);
			ahead(120);
			turnRadarRight(50);
			turnRadarRight(360);
			System.out.println(elegir);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		//ELEGIR MODO DISPARO SI FALLAMOS MUCHAS  INICIO
		if (contadorFallos == 5) {
			modoDisparo = 0;
			elegir = false;
		}
		//ELEGIR MODO DISPARO SI FALLAMOS MUCHAS FIN
		
		turnRadarRight(0);
		turnRadarLeft(0);
		turnRadarRight(e.getBearing()-(getRadarHeading() - getHeading())); //Con esto evitamos poner el radar en un extremo del cono del radar
		
		//CONFIGURACION DISPARO INICIO
		
		if(modoDisparo == 0){
			turnGunRight((getRadarHeading() - getGunHeading()));
		}else if (modoDisparo == 1) {
			turnGunRight(((getRadarHeading() - getGunHeading()) + 10));
		}
		else if (modoDisparo == 2) {
			turnGunRight(((getRadarHeading() - getGunHeading())) - 10);
		}
		
		//CONFIGURACION DISPARO FIN
		
		//DISPARO INICIO
		if(e.getDistance() < 80){
			fire(2.9);
		}
		if(e.getDistance()<160){
			if(elegir == true){
			fire(2.4);
			}else{
				fire(3);
			}
			contadorDisparos++;
		}else{
			if(elegir == true){
		fire(1.2);
			}else{
				fire(1.5);
			}
		contadorDisparos++;
		}
		//DISPARO FIN
		
		//MODO DE DISPARO INICIO
		if(elegir == true){
		if(contadorDisparos == 4){
			modoDisparo++;
			contadorDisparos = 0;
			if( modoDisparo == 3){
				elegir = false;
				if((contadorAciertosDirecto >= contadorAciertosLeft)&& (contadorAciertosDirecto >= contadorAciertosRight) ){
					modoDisparo = 0;
				}else if((contadorAciertosRight > contadorAciertosDirecto) && (contadorAciertosRight>= contadorAciertosLeft)){
					modoDisparo = 1;
				}else if ((contadorAciertosLeft>contadorAciertosDirecto) && (contadorAciertosLeft >= contadorAciertosRight)) {
					modoDisparo = 2;
				}else if((contadorAciertosDirecto == contadorAciertosLeft) && (contadorAciertosRight == contadorAciertosDirecto)){
					modoDisparo=0;
				}
			}
		}
		}
		//MODO DE DISPARO FIN
		
		enemyBearing = e.getBearing();
		turnRight(enemyBearing + 90);
		back(110);
	}
	
	
	public void onBulletMissed(BulletMissedEvent e){
		contadorFallos++;
	}
	
	public void onBulletHit(BulletHitEvent e) {
		if(elegir == true){
		if(modoDisparo == 0){
			contadorAciertosDirecto++;
		} else if(modoDisparo == 1){
			contadorAciertosRight++;
		}else if(modoDisparo == 2){
			contadorAciertosLeft++;
		}
		contadorFallos = 0;
	}
	}
	
	public void onHitRobot(HitRobotEvent e){
		turnRight(e.getBearing() +60);
		back(70);
	}
	
	public void onHitWall(HitWallEvent e) {
		turnRight(e.getBearing()-180);
		ahead(300);
		//me da mi giro respecto a la pared. A partir de esto lo ponemos a perpendicuar a la pared
		//y lo llevamos al medio
	}	
}

