/**
 * Configuration for the game
 * solace_hostUrl - WebSocket Host Address for the Solace PubSub+ Broker
 * solace_vpn - VPN for the Solace PubSub+ Broker
 * solace_userName - Username for the PubSub+ Broker
 * solace_password - Password for the PubSub+ Broker
 * allowed_ships - Ships allowed per player
 * gameboard_dimensions - Number of rows and columns for the sqare gameboard
 *
 * @author Thomas Kunnumpurath, Andrew Roberts
 */
export const gameConfig = {
  solace_hostUrl: "wss://mru4ref8tk6x3.messaging.solace.cloud:443",
  solace_vpn: "housie",
  solace_userName: "solace-cloud-client",
  solace_password: "hbk1sl1h0kp1tv0btb8e4sjg6u",
  allowed_ships: 5,
  gameboard_dimensions: 5,
  numRows: 3,
  numColumns: 9,
};
