package org.firstinspires.ftc.ppProject.treamcode;

import org.firstinspires.ftc.ppProject.company.ComputerDebugging;
import org.firstinspires.ftc.ppProject.company.FloatPoint;
import org.firstinspires.ftc.ppProject.company.Range;
import org.firstinspires.ftc.ppProject.core.Point;

import java.util.ArrayList;

import static org.firstinspires.ftc.ppProject.RobotUtilities.MovementVars.*;
import static org.firstinspires.ftc.ppProject.company.Robot.*;
import static org.firstinspires.ftc.ppProject.treamcode.MathFunctions.lineCircleIntersection;



public class RobotMovement {
    
    
    public static void followCurve(ArrayList<CurvePoint> allPoints, double followAngle) {
        for (int i = 0; i < allPoints.size()-1; i++) {
            ComputerDebugging.sendLine(new FloatPoint(allPoints.get(i).x, allPoints.get(i).y),
                    new FloatPoint(allPoints.get(i+1).x, allPoints.get(i+1).y));
            
        }
        
        CurvePoint followMe = getFollowPointPath(allPoints, new Point(worldXPosition, worldYPosition), allPoints.get(0).followDistance);
        
        ComputerDebugging.sendKeyPoint(new FloatPoint(followMe.x, followMe.y));
        goToPosition(followMe.x, followMe.y, followMe.moveSpeed, followAngle, followMe.turnSpeed);
        
    }
    
    
    
    public static CurvePoint getFollowPointPath(ArrayList<CurvePoint> pathPoints, Point robotLocation, double followRadius){
        
        CurvePoint followMe = new CurvePoint(pathPoints.get(0));
        
        for (int i = 0; i < pathPoints.size()-1; i++) {
            CurvePoint startLine = pathPoints.get(i);
            CurvePoint endLine = pathPoints.get(i + 1);
    
            ArrayList<Point> intersections = lineCircleIntersection(robotLocation, followRadius, startLine.toPoint(), endLine.toPoint());
    
            // Makes the robot turn towards intersection closest to its angle
            double closestAngle = 100000000;
    
            for (Point thisIntersection : intersections) {
                
                double angle = Math.atan2(thisIntersection.y - worldYPosition, thisIntersection.x - worldXPosition);
                // Converts angle from absolute world angle to angle relative to robot
                double deltaAngle = Math.abs(MathFunctions.angleWrap(angle - worldAngle_rad));
        
                if (deltaAngle < closestAngle) {
            
                    closestAngle = deltaAngle;
                    followMe.setPoint(thisIntersection);
            
                }
            }
        }
        
        return followMe;
        
    }
    
    
    public static void goToPosition(double x, double y, double movementSpeed, double preferredAngle, double turnSpeed) {

        double distanceToTarget= Math.hypot(x-worldXPosition, y-worldYPosition);
        double absoluteAngleToTarget = Math.atan2(y-worldYPosition, x-worldXPosition);
        double relativeAngleToPoint = MathFunctions.angleWrap(absoluteAngleToTarget - (worldAngle_rad - Math.toRadians(90)));

        double relativeXToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;
        double relativeYToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;
        
        double movementXPower = relativeXToPoint/(Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));
        double movementYPower = relativeYToPoint/(Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));

        movement_x = movementXPower*movementSpeed;
        movement_y = movementYPower*movementSpeed;

        double relativeTurnAngle = relativeAngleToPoint - Math.toRadians(180) + preferredAngle;
        movement_turn = Range.clip(relativeTurnAngle/Math.toRadians(30), -1, 1)*turnSpeed;

        if (distanceToTarget < 10)
            movement_turn = 0;

    }
    
    
    public static void myGoToPosition(double x, double y, double movementSpeed, double preferredAngle, double turnSpeed) {
    
        double distanceToTarget;
        double accuracyRange = 3;
        
        do {
            
            distanceToTarget = Math.hypot(x - worldXPosition, y - worldYPosition);
            
            double absoluteAngleToTarget = Math.atan2(y - worldYPosition, x - worldXPosition);
            double relativeAngleToPoint = MathFunctions.angleWrap(absoluteAngleToTarget - (worldAngle_rad - Math.toRadians(90)));
    
            double relativeXToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;
            double relativeYToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;
    
            double movementXPower = relativeXToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));
            double movementYPower = relativeYToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));
    
            movement_x = movementXPower * movementSpeed;
            movement_y = movementYPower * movementSpeed;
    
            double relativeTurnAngle = relativeAngleToPoint - Math.toRadians(180) + Math.toRadians(preferredAngle);
            movement_turn = Range.clip(relativeTurnAngle / Math.toRadians(30), -1, 1) * turnSpeed;
    
        } while (distanceToTarget > -accuracyRange && distanceToTarget < accuracyRange);
                movement_turn = 0;
                movement_x = 0;
                movement_y = 0;
                
    }


}
