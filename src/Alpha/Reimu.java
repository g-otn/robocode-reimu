/**
 * 6º Campeonato Robocode CPS
 * @author Gabriel Otani Pereira, Thiago Bezerra, Wagner Santos
 * @since Agosto 2018
 */
package Alpha;

import robocode.*;
import static robocode.util.Utils.*;
import java.awt.Color;

/**
 * Reimu
 */
public class Reimu extends AdvancedRobot {
  
  /*
    Variáveis de comportamento
  */
  // Movimento
  double d = 150.0;
  double dir = 1;
  // Condições de execução de movimento
  boolean saindoDaParede = false;
  
  /*
    Variáveis para armazenamento de informações sobre o alvo
  */
  String alvo = "nenhum"; // Guarda o nome do alvo
  double vidaAlvo; // Vida do alvo
  double distAlvo; // Distância do alvo
  
  /*
    Variáveis para troca de alvo
  */
  // Variáveis de controle de escolha de alvo
  boolean escolhendoAlvo = false;
  String tiroAnterior = "";
  // Variáveis de armazenamento de informações sobre outros robôs
  String outros[];
  double disOutros[];
  
  /*
    Inicia o robô, colocando configurações iniciais e escolhendo o primeiro alvo
  */
  public void run() {
    setColors(Color.RED, Color.BLACK, Color.RED, new Color(200,25,0), new Color(150,150,200));
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    prepararParaEscolherAlvo(); // Procura por alvos
    do {
      // Nunca para de andar
      setAhead((distAlvo / 4 + 25) * dir);      

      /* 
        Distanciamento da parede
      */
      // Impede de bater na parede
      if (!saindoDaParede && vaiBater(3)) { // Não tenta sair da parede se já estiver saindo dela
        dir *= -1;
        saindoDaParede = true; // Marca como estando perto da parede
      } else if (!vaiBater(3.2)) {
        saindoDaParede = false; // Marca como não estando perto da parede
      }
      
      execute();
    } while (true);
  }
  
  /*
    É chamada quando algum robô entra no arco do radar
  */
  public void onScannedRobot(ScannedRobotEvent e) {
    /*
      Escolha de alvo
    */
    if (getOthers() == 1) {
      // Escolhe como alvo já que é o único possível
      alvo = e.getName(); 
    } else { // Se houver mais de 1 inimigo sobrando
      // Guarda informações do robô para ser analizado depois (em escolherAlvo())
      if (escolhendoAlvo) {
        for (int i = 0; i < outros.length; i++) {
          if (outros[i].equals(e.getName())) {
            return; // Impede de guardar informações do mesmo robô mais de uma vez
          } else if (outros[i].equals("")) { // Se achou um espaço não preenchido do vetor (ainda não analizou todos)
            // Guardar informações
            outros[i] = e.getName();
            disOutros[i] = e.getDistance();
            out.println("Encontrado " + outros[i] + " Dist: " + Math.round(disOutros[i]*10)/10 + ")");
            if (i == outros.length-1) { // Se todos os robôs já foram escaneados
              out.println("Todos robôs analizados, escolhendo alvo");
              escolherAlvo(); // Analizar informações e escolher o alvo
            }
            i = outros.length; // Após guardar informações do robô, parar de tentar alocar
          }
        }
        return;
      } else {
        // Trocar de alvo caso esteja não tão longe comparado ao alvo e com menos vida que o alvo atual
        if (!e.getName().equals(alvo) && e.getEnergy() < vidaAlvo && e.getDistance() <= distAlvo) {
          out.println("Novo alvo por dist (" + Math.round(e.getDistance()*10.0)/10.0 + "<" + Math.round(distAlvo*10.0)/10.0 + ") e vida: " + e.getName() + " (" + Math.round(e.getEnergy()*10.0)/10.0 + "<" + Math.round(vidaAlvo*10.0)/10.0 + ")");
          alvo = e.getName();
        }
      }
    }
    
    if (!e.getName().equals(alvo)) return;
    
    if (!saindoDaParede &&
        vidaAlvo - e.getEnergy() >= Rules.MIN_BULLET_POWER && // Entre 0.1
        vidaAlvo - e.getEnergy() <= Rules.MAX_BULLET_POWER && // e 3
        !(vidaAlvo - e.getEnergy() > 0.57 && vidaAlvo - e.getEnergy() < 0.63) // dano causado por Ram
        ) {
      //out.println("Alvo perdeu energia " + Math.round(vidaAlvo * 10) / 10.0 + "->" + Math.round(e.getEnergy() * 10) / 10.0 + " (" + Math.round((vidaAlvo - e.getEnergy()) * 10) / 10.0 + "), pode ter atirado! Trocando de direção");
      dir *= -1;
    }
    
    // Troca de direção baseado na distância, para chegar perto do inimigo
    if (!vaiBater(0.6)) {
      if (e.getDistance() > getHeight()*3) {
        setTurnRight(e.getBearing() + 90 - 40*dir);
      } else {
        setTurnRight(e.getBearing() + 90 - 10*dir);
      }
    }
    
    // Ângulo de 0 até onde o alvo está
    double absBearing = e.getBearing() + getHeading();
    
    // Ajustar radar
    setTurnRadarRight(ajustarRadar(absBearing));
    
    // Mirar
    setTurnGunRightRadians(ajustarArma(e)*0.85);
    
    // Atirar
    setFire(calcularPoderTiro(e.getDistance()));
    
    // Atualização de informações sobre o alvo
    vidaAlvo = e.getEnergy();
    distAlvo = e.getDistance();
  }
  
  public void onHitByBullet(HitByBulletEvent e) {
    if (getOthers() > 1) {   
      // Troca de alvo
      if ((distAlvo > d || e.getName().equals(tiroAnterior)) && vidaAlvo > 18) {
        out.println("Novo alvo por ser atingindo! " + e.getName());
        alvo = e.getName();
        // Procura o novo alvo
        double radarBearing = normalRelativeAngleDegrees((e.getBearing() + getHeading()) - getRadarHeading());
        // Gira o radar na direção que o encontrará mais rápido
        if (radarBearing > 0) {
          setTurnRadarRight(Double.POSITIVE_INFINITY);
        } else {
          setTurnRadarRight(Double.NEGATIVE_INFINITY);
        }
      }
      
      if (!e.getName().equals(alvo)) {
        tiroAnterior = e.getName();
      }
    }
  }
  
  public void onHitRobot(HitRobotEvent e) {
    dir *= -1;    
    
    // Troca de alvo caso o robô que bateu esteja com menos vida
    if (!escolhendoAlvo && !e.getName().equals(alvo) && e.getEnergy() < vidaAlvo) {
      out.println("Novo alvo por menor vida! " + e.getName() + " (" + Math.round(e.getEnergy()*10.0)/10.0 + "<" + Math.round(vidaAlvo*10.0)/10.0 + ")");
      alvo = e.getName();
      // Procura o novo alvo
      double radarBearing = normalRelativeAngleDegrees((e.getBearing() + getHeading()) - getRadarHeading());
      // Gira o radar na direção que o encontrará mais rápido
      if (radarBearing > 0) {
        setTurnRadarRight(Double.POSITIVE_INFINITY);
      } else {
        setTurnRadarRight(Double.NEGATIVE_INFINITY);
      }
    }
  }
  
  public void onHitWall(HitWallEvent e) {
    saindoDaParede = true; // Impede de desviar perto da parede (evita movimentos errados e ficar preso)
    
    /*
      Ajuste de ângulo
    */
    if (dir == -1 && Math.abs(e.getBearing()) >= 160.0) {
      // Se estiver de costas para a parede
      dir = 1; // Sair de perto da parede andando de frente
    } else if (dir == 1 && Math.abs(e.getBearing()) <= 20.0) {
      // Se estiver de frente para a parede
      dir = -1; // Sair de perto da parede andando de costas
    } else {
      // Se estiver de lado para a parede
      if (dir == 1) {
        setTurnRight(normalRelativeAngleDegrees(e.getBearing()));
        dir = -1;
      } else {
        setTurnRight(normalRelativeAngleDegrees(e.getBearing()+180));
        dir = 1;
      }
    }
  }
  
  public void onRobotDeath(RobotDeathEvent e) {
    // Busca por outro alvo caso o atual morra
    if (e.getName().equals(alvo)) {
      // Se houver mais de um inimigo sobrando, analisá-los para escolher
      if (getOthers() > 1) {
        out.println("Alvo morreu! Procurando novo inimigo (" + getOthers() + ")");
        prepararParaEscolherAlvo();
      } else {
        out.println("Alvo morreu! Procurando último inimigo (" + getOthers() + ")");
        setTurnRadarRight(Double.POSITIVE_INFINITY);
      }
    } else if (escolhendoAlvo) {
      prepararParaEscolherAlvo();
    }
  }
  
  /*
    Prepara o robô para procurar robôs
  */
  void prepararParaEscolherAlvo() {
    // Reseta e limpa as variáveis que guardarão as informações
    outros = new String[getOthers()];
    disOutros = new double[getOthers()];
    for (int i = 0; i < outros.length; i++) {
      outros[i] = "";
    }
    escolhendoAlvo = true; // Permite guardar informação dos robos escaneados
    setTurnRadarRight(Double.POSITIVE_INFINITY); // Procura pelos robôs    
  }

  /*
    Analiza dados sobre os robôs e escolhe o alvo
  */
  void escolherAlvo() {
    // Para de escanear por novos alvos (já que já escaneou todos)
    escolhendoAlvo = false;
    
    // Escolha de alvo
    int roboEscolhido = -1;
    // Decisão baseada em distância
    double menorDis = Double.MAX_VALUE;
    for (int i = 0; i < outros.length; i++) {
      // Como é a primeira vez, todos os robôs estarão com a mesma vida, então serve como critério de escolha
      if (disOutros[i] < menorDis) {
        roboEscolhido = i;
        menorDis = disOutros[i];
      }
    }
    out.println("Novo alvo: " + outros[roboEscolhido] + " (Distância: " + Math.round(disOutros[roboEscolhido]*10.0)/10.0 + ")");
    alvo = outros[roboEscolhido];
  }
  
  /*
    Ajusta o radar para sempre estar escaneando o alvo
  */
  double ajustarRadar(double absBearing) {
    if (getOthers() > 1) {
      /*
        Radar com arco máximo: Abre o máximo possível (45°)
      */
      // Mira no inimigo abrindo o maior arco possível por turno (22.5° para cada lado)
      double correcaoRadar;
      correcaoRadar = normalRelativeAngleDegrees((absBearing - getRadarHeading()));
      correcaoRadar += 22.5*Math.signum(correcaoRadar);
      
      // Impede do arco ser maior que 45° ou muito pequeno (chance de escapar do radar)
      if (correcaoRadar > 45.0) {
        correcaoRadar = 45.0;
      } else if (correcaoRadar < -45.0) {
        correcaoRadar = -45.0;
      } else if (correcaoRadar > 0.0 && correcaoRadar < 20.0) {
        correcaoRadar = 20.0;
      } else if (correcaoRadar > -20.0 && correcaoRadar <= 0.0) {
        correcaoRadar = -20.0;
      }
      
      return correcaoRadar;
    } else {
      /*
        Radar focado: Segue o alvo com o ângulo que o encontrou (*2)
      */
      // Pega o que o radar precisa girar para alinhar e multiplica por 2,
      // pois multiplicado por 2 o radar não mira no robô e sim cobre ele com o arco criado
      return normalRelativeAngleDegrees((absBearing - getRadarHeading())*2);
    }
  }
  
  /*
    Ajusta a arma no inimigo compensando sua velocidade
  */
  double ajustarArma(ScannedRobotEvent e) {
    if (e.getEnergy() != 0.0) {
      double absBearingRad = getHeadingRadians() + e.getBearingRadians();
      // Calculo de onde o inimigo
      double compensacaoLinear = e.getVelocity() * Math.sin(e.getHeadingRadians() - absBearingRad) // Quanto o inmigo vai andar
              / Rules.getBulletSpeed(calcularPoderTiro(e.getDistance())); // Quanto tempo a bala demora para alcançar
      
      // Modificação de compensação linear (para não perder muito tempo virando a arma se estiver perto)
      if (e.getDistance() <= 12*10) {
        // Se estiver perto
        compensacaoLinear *= 0.5;
      } else if (e.getDistance() <= 12*5) {
        // Se estiver muito perto
        compensacaoLinear *= 0.3;
      }
      
      return normalRelativeAngle(absBearingRad - getGunHeadingRadians() + compensacaoLinear);
    } else {
      // Se o inimigo estiver com 0 de energia (Disabled), ele fica parado mas a velocidade pode estar maior que 0
      // então é preciso desligar a mira lateral, que usa a velocidade para determinar o ângulo
      return normalRelativeAngle(getHeadingRadians()+ e.getBearingRadians()- getGunHeadingRadians());
    }
  }
  
  /*
    Calcula a potência do tiro baseado na distância do alvo e na própria vida
  */
  double calcularPoderTiro(double dAlvo) {
    if (vidaAlvo != 0.0) {
      if (dAlvo < getHeight()*1.5) {
        // Se estiver perto o suficiente "para não errar"
        return Rules.MAX_BULLET_POWER;
      } else if (getEnergy() > 4*Rules.MAX_BULLET_POWER + 2*(Rules.MAX_BULLET_POWER - 1)
              || (getOthers() == 1 && getEnergy() > 3*Rules.MAX_BULLET_POWER)) {
        // Se estiver com vida suficiente para atirar forte
        if (dAlvo <= d*2) {
          // Perto
          return Rules.MAX_BULLET_POWER;
        } else {
          // Longe
          return Math.min(1.1 + (d*2) / dAlvo, Rules.MAX_BULLET_POWER); // Quanto mais longe, menor o poder de tiro
        }
      } else if (getEnergy() > 2.2) {
        // Se estiver com a vida baixa
        return 1.1; // Quanto mais longe, menor o poder de tiro
      } else {
        return Math.max(0.1, getEnergy()/3);
      }
    } else {
      return 0.1;
    }
  }
  
  
  /*
    Checa se o robô vai bater na parede
  */
  boolean vaiBater(double margem) {
    // Retorna verdadeiro se a posição estiver chegando perto das paredes, caso contrário retorna falso
    return getX() + getHeight()*margem >= getBattleFieldWidth()
            || getX() - getHeight()*margem <= 0.0
            || getY() + getHeight()*margem >= getBattleFieldHeight()
            || getY() - getHeight()*margem <= 0.0;
  }
}
