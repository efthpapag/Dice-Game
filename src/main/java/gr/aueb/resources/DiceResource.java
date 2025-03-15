package gr.aueb.resources;

import gr.aueb.domain.Dice;
import gr.aueb.representation.AnswerRepresentation;
import gr.aueb.representation.DiceNumberRepresentation;
import gr.aueb.representation.StringRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.security.NoSuchAlgorithmException;

@RequestScoped
@Path("/dice-game")
public class DiceResource {

    @Inject
    Dice dice;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/start-game")
    public Response startGame() throws NoSuchAlgorithmException {

        dice.rollDice();

        dice.createRandomString();

        System.out.println(jwt.getSubject());

        return Response.ok().build();
    }

    @POST
    @Path("/sendRClient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateCommit(StringRepresentation RClient) throws NoSuchAlgorithmException {
        dice.setRClient(RClient.value);
        dice.createHash();
        StringRepresentation commit = new StringRepresentation();
        commit.value = dice.getServerHash();
        return Response.ok().entity(commit).build();
    }

    @POST
    @Path("/determinewinner")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response determineWinner(DiceNumberRepresentation diceClientRepresentation) {
        AnswerRepresentation answerRepresentation = new AnswerRepresentation();
        /*if(diceClientRepresentation.diceNumber > dice.getDiceResult()) {
            answerRepresentation.condition = "WON";
        }
        else if(diceClientRepresentation.diceNumber < dice.getDiceResult()) {
            answerRepresentation.condition = "LOST";
        }
        else {answerRepresentation.condition = "TIED";}*/

        if(diceClientRepresentation.diceNumber == dice.getDiceResult()) {
            answerRepresentation.condition = "WON";
        }
        else {
            answerRepresentation.condition = "LOST";
        }

        answerRepresentation.diceResult = dice.getDiceResult();
        answerRepresentation.rServer = dice.getRServer();
        return Response.ok().entity(answerRepresentation).build();
    }
}
