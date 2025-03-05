package gr.aueb.resources;

import gr.aueb.domain.Dice;
import gr.aueb.representation.AnswerRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import gr.aueb.representation.StringRepresentation;
import gr.aueb.representation.DiceNumberRepresentation;

import java.security.NoSuchAlgorithmException;

@RequestScoped
@Path("/dice-game")
public class DiceResource {

    @Inject
    Dice dice;

    @POST
    @Path("/start-game")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startGame(StringRepresentation rClientRepresentation) throws NoSuchAlgorithmException {

        System.out.println(rClientRepresentation);
        dice.setRClient(rClientRepresentation.value);

        dice.rollDice();

        dice.createRandomString();

        dice.createHash();
        System.out.println(dice.getDiceResult());
        System.out.println(dice.getRServer());
        System.out.println(dice.getRClient());

        StringRepresentation hashValue = new StringRepresentation();
        hashValue.value = dice.getServerHash();

        return Response.ok().entity(hashValue).build();
    }

    @POST
    @Path("/sendDiceOfClient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendDiceOfClient(DiceNumberRepresentation diceClientRepresentation) {
        AnswerRepresentation answerRepresentation = new AnswerRepresentation();
        if(diceClientRepresentation.diceNumber > dice.getDiceResult()) {
            answerRepresentation.condition = "WON";
        }
        else if(diceClientRepresentation.diceNumber < dice.getDiceResult()) {
            answerRepresentation.condition = "LOST";
        }
        else {answerRepresentation.condition = "TIED";}

        answerRepresentation.diceResult = dice.getDiceResult();
        answerRepresentation.rServer = dice.getRServer();
        return Response.ok().entity(answerRepresentation).build();
    }
}
