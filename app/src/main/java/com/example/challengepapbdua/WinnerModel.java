package com.example.challengepapbdua;

public class WinnerModel {
    private String status;
    private String yourResult;
    private String computerResult;

    public WinnerModel(String status, String yourResult, String computerResult) {
        this.status = status;
        this.yourResult = yourResult;
        this.computerResult = computerResult;
    }

    public WinnerModel() {

    }

    public void setStatus (String status) {
        this.status = status;
    }

    public void setYourResult (String yourResult) {
        this.yourResult = yourResult;
    }

    public void setComputerResult (String computerResult) {
        this.computerResult = computerResult;
    }

    public String getStatus() {
        return status;
    }

    public String getYourResult() {
        return yourResult;
    }

    public String getComputerResult() {
        return computerResult;
    }
}
