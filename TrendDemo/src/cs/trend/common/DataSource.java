package cs.trend.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for getting telemetry data from a remote server or service.
 * 
 * @author hvhong
 *
 */
public interface DataSource extends Remote {

	<T> T get(Telemetry<T> t) throws RemoteException;
}
