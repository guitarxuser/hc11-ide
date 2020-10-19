package hc11_ide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.omg.CORBA.portable.InputStream;

public class invoke_shell_command {
	ProcessBuilder processBuilder = new ProcessBuilder();
   String port_name=Mywin.port_name;
	public invoke_shell_command() { 
		// TODO Auto-generated constructor stub
		//Process process = new ProcessBuilder("C:\\PathToExe\\MyExe.exe","param1","param2").start();
		// Run a shell command
		//processBuilder.command("bash", "-c", "echo $'\ca' >/dev/ttyUSB0");
		System.out.println(port_name);
		String cmd="echo"+" $'\\ca' >"+ port_name;
		System.out.println(cmd);
		processBuilder.command("bash", "-c", cmd);
		//processBuilder.command("bash", "-c", "ls /home");
		
		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				System.out.println(output);
				//System.exit(0);
			} else {
				//abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
