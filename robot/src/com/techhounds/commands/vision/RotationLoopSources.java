package com.techhounds.commands.vision;

import com.techhounds.subsystems.DriveSubsystem;
import com.techhounds.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class RotationLoopSources implements PIDSource, PIDOutput {

	@Override
	public void pidWrite(double output) {
		/*if(Math.abs(output) < MIN_TURN_POWER){
			if(pid.onTarget()){
				output = 0;
			}else if(output > 0){
				output = MIN_TURN_POWER;
			}else if(output < 0){
				output = -MIN_TURN_POWER;
			}
		}*/
		
		DriveSubsystem.getInstance().rotateWithPower(output);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {}

	@Override
	public PIDSourceType getPIDSourceType() {	return PIDSourceType.kDisplacement;	}

	@Override
	public double pidGet() {
		return GyroSubsystem.getInstance().getRotation();
	}
}
