<%@ include file="/include-internal.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>

<jsp:useBean id="name" class="sferencik.teamcity.clutterstats.Names"/>

<l:settingsGroup title="ClutterStats parameters">
    <tr>
        <td>Directory to measure</td>
        <td><props:textProperty name="${name.directoryPathParameterName}" /></td>
    </tr>
    <tr>
        <td>Name of the parameter to set, and the metric to publish</td>
        <td><props:textProperty name="${name.parameterNameParameterName}" /></td>
    </tr>
</l:settingsGroup>
