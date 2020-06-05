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
  solace_hostUrl: process.env.hostUrl,
  solace_vpn: process.env.vpn,
  solace_userName: process.env.userName,
  solace_password: process.env.password,
  allowed_ships: 5,
  gameboard_dimensions: 5,
  numRows: 3,
  numColumns: 9,
};
