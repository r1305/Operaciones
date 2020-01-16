package com.system.operaciones.response;

public class RespuestaResponse {
    int ide_error;
    String msg_error;
    String respuesta;
    String ver_android;

    public RespuestaResponse() {
    }

    public int getIde_error() {
        return ide_error;
    }

    public void setIde_error(int ide_error) {
        this.ide_error = ide_error;
    }

    public String getDes_error() {
        return msg_error;
    }

    public void setDes_error(String des_error) {
        this.msg_error = des_error;
    }

    public Object getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getVer_android() {
        return ver_android;
    }

    public void setVer_android(String ver_android) {
        this.ver_android = ver_android;
    }
}
