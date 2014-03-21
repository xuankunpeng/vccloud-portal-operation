package com.vccloud.portal.vo;

import net.sf.json.JSONObject;

import com.vccloud.portal.util.CommonUtil;
import com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.Room;
import com.vccloud.portal.webservice.VidyoPortalAdminService.VidyoPortalAdminServiceStub.RoomMode_type0;

public class RoomVO {

	private Room room;

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		if (room == null) {
			return json;
		}
		json.put("name", CommonUtil.null2Empty(room.getName()));
		json.put("groupName", CommonUtil.null2Empty(room.getGroupName()));
		RoomMode_type0 roomMode = room.getRoomMode();
		if (roomMode != null && roomMode.getHasPIN()
				&& roomMode.getRoomPIN() != null) {
			json.put("roomPIN", roomMode.getRoomPIN());
		} else {
			json.put("roomPIN", "");
		}
		json.put("roomID", room.getRoomID().getEntityID());
		return json;
	}

	public JSONObject toJson2() {
		JSONObject json = new JSONObject();
		if (room == null) {
			return json;
		}
		json.put("name", CommonUtil.null2Empty(room.getName()));
		json.put("groupName", CommonUtil.null2Empty(room.getGroupName()));
		json.put("extension", CommonUtil.null2Empty(room.getExtension()));
		json.put("roomType", CommonUtil.null2Empty(room.getRoomType()
				.getValue()));
		json.put("roomID", room.getRoomID().getEntityID());
		return json;
	}

	public JSONObject toJson3() {
		JSONObject json = new JSONObject();
		if (room == null) {
			return json;
		}
		json.put("description", CommonUtil.null2Empty(room.getDescription()));
		json.put("extension", CommonUtil.null2Empty(room.getExtension()));
		json.put("groupName", CommonUtil.null2Empty(room.getGroupName()));
		json.put("name", CommonUtil.null2Empty(room.getName()));
		json.put("ownerName", CommonUtil.null2Empty(room.getOwnerName()));
		json.put("roomID", room.getRoomID().getEntityID());
		json.put("hasModeratorPIN", room.getRoomMode().getHasModeratorPIN());
		json.put("hasPIN", room.getRoomMode().getHasPIN());
		json.put("isLocked", room.getRoomMode().getIsLocked());
		json.put("moderatorPIN", CommonUtil.null2Empty(room.getRoomMode()
				.getModeratorPIN()));
		json.put("roomPIN", CommonUtil.null2Empty(room.getRoomMode()
				.getRoomPIN()));
		json.put("roomURL", CommonUtil.null2Empty(room.getRoomMode()
				.getRoomURL()));
		json.put("roomType", CommonUtil.null2Empty(room.getRoomType()
				.getValue()));
		return json;
	}

}
