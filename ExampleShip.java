import java.awt.Color;

import ihs.apcs.spacebattle.*;
import ihs.apcs.spacebattle.commands.*;

public class ExampleShip extends BasicSpaceship {
    private int currWidth;
    private int currHeight;
    private Point midpoint;
    private boolean radarRan = false;
    private double start = System.currentTimeMillis();
    private boolean ababa = false;
    
    public static void main(String[] args)
    {
        TextClient.run("10.56.98.121", new ExampleShip());
    }

    @Override
    public RegistrationData registerShip(int numImages, int worldWidth, int worldHeight)
    {
        this.currWidth = worldWidth;
        this.currHeight = worldHeight;
        this.midpoint = new Point(worldWidth/2, worldHeight/2);
        return new RegistrationData("Ship1", new Color(255, 255, 255), 0);
    }

    @Override
    public ShipCommand getNextCommand(BasicEnvironment env)
    {
        if (!(radarRan)) {
            System.out.println("scan");
            radarRan = true;
            return new RadarCommand(4);
        }
        if (start + 2500<=System.currentTimeMillis()) {
            System.out.println("rescan start");
            start += 2500;
            radarRan = false;
        }
        ObjectStatus ship = env.getShipStatus();
        RadarResults radar = env.getRadar();
        if (!(ababa) && ship.getSpeed()>10) {
            ababa = true;
            return new DeployLaserBeaconCommand();
        }
        if (ship.getHealth()<80 && ship.getEnergy()>=20) {
            return new RepairCommand(20);
        } else if (ship.getSpeed() < 67 && ship.getEnergy()>50) {
            ababa = false;
            return new ThrustCommand('B', .3, 1.0);
        } else if (ship.getSpeed() > 15 && ship.getEnergy()>50) {
            return new RotateCommand(ship.getPosition().getAngleTo(midpoint) - ship.getOrientation());

        }
        return new FireTorpedoCommand('F');
    }
}