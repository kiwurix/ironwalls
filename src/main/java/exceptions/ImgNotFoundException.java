package exceptions;

public class ImgNotFoundException extends Exception{
    public ImgNotFoundException(String imgName) {
        super(imgName+" image " +"not found");
    }
}
