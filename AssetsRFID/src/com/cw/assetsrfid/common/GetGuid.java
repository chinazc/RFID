package com.cw.assetsrfid.common;

import java.util.UUID;

public class GetGuid {

	public static final String GenerateGUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

}
