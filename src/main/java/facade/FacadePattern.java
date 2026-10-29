package facade;

class PowerSupply {
    public void providePower(){
        System.out.println("Power supply");
    }
}

class CoolingSupply {
    public void provideCooling(){
        System.out.println("Cooling supply");
    }
}

class CPU {
    public void initialise(){
        System.out.println("CPU initialise");
    }
}

class Memory {
    public void selftest(){
        System.out.println("Memory initialise");
    }
}

class HardDrive {
    public void spinUp() {
        System.out.println("Hard Drive: Spinning up...");
    }
}

class BIOS {
    public void boot(CPU cpu, Memory memory) {
           System.out.println("BIOS Boot: Booting...");
           cpu.initialise();
           memory.selftest();
    }
}

class OperatingSystem {
    public void load() {
        System.out.println("Operating System: Loading into memory...");
    }
}


//Facade
class ComputerFacade {
    private PowerSupply powerSupply = new PowerSupply();
    private CoolingSupply coolingSupply = new CoolingSupply();
    private CPU cpu = new CPU();
    private Memory memory = new Memory();
    private HardDrive hardDrive = new HardDrive();
    private BIOS bios = new BIOS();
    private OperatingSystem operatingSystem = new OperatingSystem();

    public void startComputer(){
        powerSupply.providePower();
        coolingSupply.provideCooling();
        cpu.initialise();
        bios.boot(cpu, memory);
        operatingSystem.load();
        hardDrive.spinUp();
        memory.selftest();
    }
}

//Client
public class FacadePattern{
       public static void main(String[] args) {
           ComputerFacade computerFacade = new ComputerFacade();
           computerFacade.startComputer();
       }
}
