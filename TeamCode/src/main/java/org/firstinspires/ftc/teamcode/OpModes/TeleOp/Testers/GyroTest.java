package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.OLD.OLDRobot;


@TeleOp(name = "Gyro Test")
@Disabled
public class GyroTest extends OpMode {

    OLDRobot robot = new OLDRobot(this);



    @Override
    public void init() {

    }



    @Override
    public void loop() {
        telemetry.addData("Deg", robot.sensors.getWorldAngleDeg());
        telemetry.update();

    }



}
