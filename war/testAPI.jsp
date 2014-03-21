<%@ page pageEncoding="UTF-8"%>
<html>
	<head>
		<title>VcCloud Portal API</title>
	</head>
	<body>
		<h3>Sign In</h3>
		<form action="user/signIn.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			username:<input type="text" name="username"><br />
			password:<input type="text" name="password"><br />
			portalUrl:<input type="text" name="portalUrl"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Sign Out</h3>
		<form action="user/signOut.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Conference Call2s</h3>
		<form action="vidyo/searchConferenceCall2s.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantName:<input type="text" name="tenantName"><br />
			callerName:<input type="text" name="callerName"><br />
			startTime:<input type="text" name="startTime"><br />
			endTime:<input type="text" name="endTime"><br />
			orderByClause:<input type="text" name="orderByClause"><br />
			pageSize:<input type="text" name="pageSize"><br />
			pageNo:<input type="text" name="pageNo"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Store Portal Info</h3>
		<form action="vidyo/storePortalInfo.do" method="post" enctype="multipart/form-data">
			client:<input type="text" name="client" value="callback"><br />
			portalUrl:<input type="text" name="portalUrl"><br />
			welcomeWord:<input type="text" name="welcomeWord"><br />
			file:<input type="file" name="file"> （支持“bmp“, “jpg“, “wbmp“, “jpeg“, “png“格式）<br>
			callbackURL:<input type="text" name="callbackURL"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Portal Info</h3>
		<form action="vidyo/getPortalInfo.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			portalUrl:<input type="text" name="portalUrl"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Members</h3>
		<form action="user/searchMembers.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			name:<input type="text" name="name"><br />
			pageSize:<input type="text" name="pageSize"><br />
			pageNo:<input type="text" name="pageNo"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Member Password</h3>
		<form action="user/updateMemberPassword.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			memberId:<input type="text" name="memberId"><br />
			newPassword:<input type="text" name="newPassword"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Password</h3>
		<form action="user/updatePassword.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			password:<input type="text" name="password"><br />
			newPassword:<input type="text" name="newPassword"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Display Name</h3>
		<form action="user/updateDisplayName.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			displayName:<input type="text" name="displayName"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Member Display Name</h3>
		<form action="user/updateMemberDisplayName.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			memberId:<input type="text" name="memberId"><br />
			displayName:<input type="text" name="displayName"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Rooms</h3>
		<form action="vidyo/searchRooms.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Room Name</h3>
		<form action="vidyo/updateRoomName.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			roomId:<input type="text" name="roomId"><br />
			name:<input type="text" name="name"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Room Pin</h3>
		<form action="vidyo/updateRoomPin.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			roomId:<input type="text" name="roomId"><br />
			pin:<input type="text" name="pin"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Clear Room</h3>
		<form action="vidyo/clearRoom.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			roomId:<input type="text" name="roomId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Store Legacy</h3>
		<form action="vidyo/storeLegacy.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			id:<input type="text" name="id"><br />
			url:<input type="text" name="url"><br />
			name:<input type="text" name="name"><br />
			extension:<input type="text" name="extension"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Legacies</h3>
		<form action="vidyo/getLegacies.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			url:<input type="text" name="url"><br />
			roomId:<input type="text" name="roomId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Assign Legacies</h3>
		<form action="vidyo/assignLegacies.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			url:<input type="text" name="url"><br />
			roomId:<input type="text" name="roomId"><br />
			legacyIds:<input type="text" name="legacyIds"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Delete Legacy</h3>
		<form action="vidyo/deleteLegacy.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			id:<input type="text" name="id"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Legacies</h3>
		<form action="vidyo/searchLegacies.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			keyword:<input type="text" name="keyword"><br />
			pageSize:<input type="text" name="pageSize"><br />
			pageNo:<input type="text" name="pageNo"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Room Profile</h3>
		<form action="vidyo/getRoomProfile.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			roomId:<input type="text" name="roomId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Store Room Profile</h3>
		<form action="vidyo/storeRoomProfile.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			roomId:<input type="text" name="roomId"><br />
			roomProfileName:<input type="text" name="roomProfileName"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Reset Room URL</h3>
		<form action="vidyo/resetRoomURL.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			roomId:<input type="text" name="roomId"><br />
			<input type="submit" value="Submit"><br />
		</form>

		<h1 style="color:red">Operation Interfaces</h1><br />

		<h3>SignIn</h3>
		<form action="operation/user/signIn.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			username:<input type="text" name="username"><br />
			password:<input type="text" name="password"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>SignOut</h3>
		<form action="operation/user/signOut.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Store Portal</h3>
		<form action="operation/vidyo/storePortal.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			id:<input type="text" name="id"><br />
			portalName:<input type="text" name="portalName"><br />
			portalUrl:<input type="text" name="portalUrl"><br />
			superName:<input type="text" name="superName"><br />
			superPassword:<input type="text" name="superPassword"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Portal</h3>
		<form action="operation/vidyo/getPortal.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			id:<input type="text" name="id"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Delete Portal</h3>
		<form action="operation/vidyo/deletePortal.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			id:<input type="text" name="id"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Portals</h3>
		<form action="operation/vidyo/searchPortals.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			keyword:<input type="text" name="keyword"><br />
			pageSize:<input type="text" name="pageSize"><br />
			pageNo:<input type="text" name="pageNo"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Tenants</h3>
		<form action="operation/vidyo/searchTenants.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			keyword:<input type="text" name="keyword"><br />
			portalId:<input type="text" name="portalId"><br />
			pageSize:<input type="text" name="pageSize"><br />
			pageNo:<input type="text" name="pageNo"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Prepare 4 Store Tenant</h3>
		<form action="operation/vidyo/prepare4StoreTenant.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			portalId:<input type="text" name="portalId"><br />
			tenantId:<input type="text" name="tenantId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Store Tenant</h3>
		<form action="operation/vidyo/storeTenant.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			portalId:<input type="text" name="portalId"><br />
			tenantId:<input type="text" name="tenantId"><br />
			tenantName:<input type="text" name="tenantName"><br />
			tenantURL:<input type="text" name="tenantURL"><br />
			tenantAdminName:<input type="text" name="tenantAdminName"><br />
			tenantAdminPassword:<input type="text" name="tenantAdminPassword"><br />
			extensionPrefix:<input type="text" name="extensionPrefix"><br />
			dialinNumber:<input type="text" name="dialinNumber"><br />
			vidyoReplayUrl:<input type="text" name="vidyoReplayUrl"><br />
			description:<input type="text" name="description"><br />
			numOfInstalls:<input type="text" name="numOfInstalls"><br />
			numOfSeats:<input type="text" name="numOfSeats"><br />
			numOfLines:<input type="text" name="numOfLines"><br />
			numOfExecutives:<input type="text" name="numOfExecutives"><br />
			numOfPanoramas:<input type="text" name="numOfPanoramas"><br />
			enableGuestLogin:<input type="text" name="enableGuestLogin"><br />
			allowedTenantList:<input type="text" name="allowedTenantList"><br />
			vidyoManager:<input type="text" name="vidyoManager"><br />
			vidyoProxyList:<input type="text" name="vidyoProxyList"><br />
			allowedVidyoGatewayList:<input type="text" name="allowedVidyoGatewayList"><br />
			allowedVidyoReplayRecorderList:<input type="text" name="allowedVidyoReplayRecorderList"><br />
			allowedVidyoReplayList:<input type="text" name="allowedVidyoReplayList"><br />
			allowedLocationTagList:<input type="text" name="allowedLocationTagList"><br />
			vidyoMobileAllowed:<input type="text" name="vidyoMobileAllowed"><br />
			ipcAllowOutbound:<input type="text" name="ipcAllowOutbound"><br />
			ipcAllowInbound:<input type="text" name="ipcAllowInbound"><br />
			portalURL:<input type="text" name="portalURL"><br />
			acl:<input type="text" name="acl"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Check 4 Store Tenant</h3>
		<form action="operation/vidyo/check4StoreTenant.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			tenantName:<input type="text" name="tenantName"><br />
			tenantURL:<input type="text" name="tenantURL"><br />
			extensionPrefix:<input type="text" name="extensionPrefix"><br />
			portalURL:<input type="text" name="portalURL"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Tenant</h3>
		<form action="operation/vidyo/getTenant.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Delete Tenant</h3>
		<form action="operation/vidyo/deleteTenant.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Members</h3>
		<form action="operation/user/searchMembers.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			keyword:<input type="text" name="keyword"><br />
			tenantId:<input type="text" name="tenantId"><br />
			pageSize:<input type="text" name="pageSize"><br />
			pageNo:<input type="text" name="pageNo"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Store Member</h3>
		<form action="operation/user/storeMember.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			memberId:<input type="text" name="memberId"><br />
			tenantId:<input type="text" name="tenantId"><br />
			name:<input type="text" name="name"><br />
			password:<input type="text" name="password"><br />
			displayName:<input type="text" name="displayName"><br />
			email:<input type="text" name="email"><br />
			extension:<input type="text" name="extension"><br />
			groupName:<input type="text" name="groupName"><br />
			proxyName:<input type="text" name="proxyName"><br />
			locationTag:<input type="text" name="locationTag"><br />
			language:<input type="text" name="language"><br />
			description:<input type="text" name="description"><br />
			roleName:<input type="text" name="roleName"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Prepare 4 Store Member</h3>
		<form action="operation/user/prepare4StoreMember.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Delete Member</h3>
		<form action="operation/user/deleteMember.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			memberId:<input type="text" name="memberId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Member Password</h3>
		<form action="operation/user/updateMemberPassword.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			memberId:<input type="text" name="memberId"><br />
			newPassword:<input type="text" name="newPassword"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Member</h3>
		<form action="operation/user/getMember.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			memberId:<input type="text" name="memberId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Rooms</h3>
		<form action="operation/vidyo/searchRooms.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Store Room</h3>
		<form action="operation/vidyo/storeRoom.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			roomId:<input type="text" name="roomId"><br />
			description:<input type="text" name="description"><br />
			extension:<input type="text" name="extension"><br />
			groupName:<input type="text" name="groupName"><br />
			name:<input type="text" name="name"><br />
			ownerName:<input type="text" name="ownerName"><br />
			roomType:<input type="text" name="roomType"> ('Personal', 'Public' or empty)<br />
			roomPIN:<input type="text" name="roomPIN"><br />
			moderatorPIN:<input type="text" name="moderatorPIN"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Room</h3>
		<form action="operation/vidyo/getRoom.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			roomId:<input type="text" name="roomId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Delete Room</h3>
		<form action="operation/vidyo/deleteRoom.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			roomId:<input type="text" name="roomId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Prepare 4 Store Room</h3>
		<form action="operation/vidyo/prepare4StoreRoom.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Room Pin</h3>
		<form action="operation/vidyo/updateRoomPin.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			roomId:<input type="text" name="roomId"><br />
			pin:<input type="text" name="pin"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Update Room Moderator Pin</h3>
		<form action="operation/vidyo/updateRoomModeratorPin.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			roomId:<input type="text" name="roomId"><br />
			pin:<input type="text" name="pin"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Reset Room URL</h3>
		<form action="operation/vidyo/resetRoomURL.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			roomId:<input type="text" name="roomId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Clear Room</h3>
		<form action="operation/vidyo/clearRoom.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			roomId:<input type="text" name="roomId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Groups</h3>
		<form action="operation/vidyo/getGroups.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Group</h3>
		<form action="operation/vidyo/getGroup.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			groupId:<input type="text" name="groupId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Delete Group</h3>
		<form action="operation/vidyo/deleteGroup.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			groupId:<input type="text" name="groupId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Store Group</h3>
		<form action="operation/vidyo/storeGroup.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			tenantId:<input type="text" name="tenantId"><br />
			groupId:<input type="text" name="groupId"><br />
			name:<input type="text" name="name"><br />
			description:<input type="text" name="description"><br />
			roomMaxUsers:<input type="text" name="roomMaxUsers"><br />
			userMaxBandWidthIn:<input type="text" name="userMaxBandWidthIn"><br />
			userMaxBandWidthOut:<input type="text" name="userMaxBandWidthOut"><br />
			allowRecording:<input type="text" name="allowRecording"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Get Tenants By Portal Config Id</h3>
		<form action="operation/vidyo/getTenantsByPortalConfigId.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			portalConfigId:<input type="text" name="portalConfigId"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>Search Cdrs</h3>
		<form action="operation/vidyo/searchCdrs.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			portalId:<input type="text" name="portalId"><br />
			tenantId:<input type="text" name="tenantId"><br />
			memberId:<input type="text" name="memberId"><br />
			startTime:<input type="text" name="startTime"><br />
			endTime:<input type="text" name="endTime"><br />
			pageSize:<input type="text" name="pageSize"><br />
			pageNo:<input type="text" name="pageNo"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<form action="operation/vidyo/searchCdrs.do" method="post">
			client:<input type="text" name="client" value="jsonp"><br />
			portalId:<input type="text" name="portalId"><br />
			tenantId:<input type="text" name="tenantId"><br />
			memberId:<input type="text" name="memberId"><br />
			startTime:<input type="text" name="startTime"><br />
			endTime:<input type="text" name="endTime"><br />
			pageSize:<input type="text" name="pageSize"><br />
			pageNo:<input type="text" name="pageNo"><br />
			<input type="submit" value="Submit"><br />
		</form>
		<h3>import Member</h3>
		<form action="operation/user/importMember.do" method="post" enctype="multipart/form-data">
			client:<input type="text" name="client" value="callback"><br />
			file:<input type="file" name="file"><br />
			tenantId:<input type="text" name="tenantId"><br />
			callbackURL:<input type="text" name="callbackURL"><br />
			<input type="submit" value="Submit"><br />
		</form>
	</body>
</html>
