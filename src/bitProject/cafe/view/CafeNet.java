package bitProject.cafe.view;

import bitProject.cafe.dto.CafeDTO;

public interface CafeNet {

	public void request(CafeDTO cafeDTO);

	public void connectToServer();

	public void disconnect();

	public Object response();
}
