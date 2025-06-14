package io.megafair.jinfra.vibra.shared.gameprovider.player;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

/**
 * Launch Parameters - Query string parameters received by the client application
 * These are URL parameters passed when launching the game
 */
@Data
public class LaunchParameters {
    
    /**
     * Operator identifier. This will be unique for each operator and defined at VRGS.
     * Example: 6ecc50b3-0123-a123-b234-39c3eef3dbdc
     */
    @QueryParam("siteId")
    private String siteId;
    
    /**
     * Game name identifier. This will be unique for each game.
     */
    @QueryParam("gameId")
    private String gameId;
    
    /**
     * Currency. Following ISO 4217 plus additional sets 
     * (i.e.: DEC for 2 decimal places with no currency symbol).
     */
    @QueryParam("currency")
    private String currency;
    
    /**
     * Based on ISO 639. 2 letters code that identifies the language to be displayed
     * ("en" for English, "el" for greek, etc).
     */
    @QueryParam("locale")
    private String locale;
    
    /**
     * Fun or Real mode ("fun", "real").
     */
    @QueryParam("gameMode")
    private String gameMode;
    
    /**
     * VRGS URL. This parameter is to be informed to the RGS platform 
     * so that it can start communicating with the VRGS.
     */
    @QueryParam("serverAddress")
    private String serverAddress;
    
    /**
     * VRGS wrapper bridge application to communicate with the operator.
     */
    @QueryParam("apiAddress")
    private String apiAddress;
    
    /**
     * "mobile" or "desktop" depending where it is being launched.
     */
    @QueryParam("channel")
    private String channel;
    
    /**
     * Character that will be used to separate numbers grouping ("," "." etc)
     */
    @QueryParam("groupingSeparator")
    private String groupingSeparator;
    
    /**
     * Character that will be used to separate decimal places ("," "." etc)
     */
    @QueryParam("decimalSeparator")
    private String decimalSeparator;
    
    /**
     * Game Rules html file URL to be called from the wrapper when needed.
     */
    @QueryParam("gameRulesURL")
    private String gameRulesURL;
    
    /**
     * Security token. Different in every launching of game
     */
    @QueryParam("token")
    private String token;
    
    /**
     * If it is added in our backoffice, is the game ID defined by the vendor
     */
    @QueryParam("providerGameId")
    private String providerGameId;
    
    /**
     * Self-explanatory
     */
    @QueryParam("userId")
    private String userId;
    
    /**
     * Casino operator identifier. This will be unique for each operator and defined at VRGS.
     * Example: vibraoperator
     */
    @QueryParam("casinoSiteId")
    private String casinoSiteId;
}
