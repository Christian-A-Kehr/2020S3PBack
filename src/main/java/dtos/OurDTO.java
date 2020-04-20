/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author Christian
 */
public class OurDTO {

    private String firstApi, firstRef, secondApi, secondRef, thirdApi, thirdRef,
            fourthApi, fourthRef, fifthApi, fifthRef;

    public OurDTO(ChuckDTO cDTO, DadDTO dDTO) {
        this.firstApi = cDTO.getValue();
        this.firstRef = cDTO.getUrl();
        this.secondApi = dDTO.getJoke();
        this.secondRef = "https://icanhazdadjoke.com/";
    }

    public OurDTO(ChuckDTO cDTO1, ChuckDTO cDTO2, ChuckDTO cDTO3, ChuckDTO cDTO4, DadDTO dDTO) {
        this.firstApi = cDTO1.getValue();
        this.firstRef = cDTO1.getUrl();
        this.secondApi = cDTO2.getValue();
        this.secondRef = cDTO2.getUrl();
        this.thirdApi = cDTO3.getValue();
        this.thirdRef = cDTO3.getUrl();
        this.fourthApi = cDTO4.getValue();
        this.fourthRef = cDTO4.getUrl();
        this.fifthApi = dDTO.getJoke();
        this.fifthRef = "https://icanhazdadjoke.com/";
    }

    @Override
    public String toString() {
        return "OurDTO{" + "firstApi=" + firstApi + ", firstRef=" + firstRef + ", secondApi=" + secondApi + ", secondRef=" + secondRef + ", thirdApi=" + thirdApi + ", thirdRef=" + thirdRef + ", fourthApi=" + fourthApi + ", fourthRef=" + fourthRef + ", fifthApi=" + fifthApi + ", fifthRef=" + fifthRef + '}';
    }


   
}
