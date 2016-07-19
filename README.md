# 2016-Robot

### This Project Contains
- 2016 Robot Project (in Java)
- Robot Project Utilities
- Trajectory Configuration for 2016

### Instructions
- Recursively Clone 2016-Robot
```{r, engine='bash', count_lines}
  git clone --recursive https://github.com/frc868/2016-Robot
```

- In Eclipse, select your workspace to /path-to-2016-Robot/2016-Robot
- IMPORT all Eclipse projects within /path-to-2016-Robot/2016-Robot
- If an error shows up about network tables or wpilib variables not being configured, create a blank Robot project and then delete it.

### Updating Submodules 
```{r, engine='bash', count_lines}
  cd /path-to-2016-Robot/2016-Robot/
  git submodule update --recursive
```
