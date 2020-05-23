/**
 * Class that represents the various parameters for a game
 * allowedShips: The number of ships allowed
 * gameboardDimensions: The number of rows and columns for the gameboard
 *
 */
export class GameParams {
  constructor(private _allowedShips: number, private _gameboardDimensions: number, private _numRows: number, private _numColumns: number) {}

  get allowedShips(): number {
    return this._allowedShips;
  }

  get gameboardDimensions(): number {
    return this._gameboardDimensions;
  }

  set allowedShips(allowedShips: number) {
    this._allowedShips = allowedShips;
  }

  set gameboardDimensions(gameboardDimensions: number) {
    this._gameboardDimensions = gameboardDimensions;
  }

  get numRows(): number {
    return this._numRows;
  }

  get numColumns(): number {
    return this._numColumns;
  }

  set numRows(numRows: number) {
    this._numRows = numRows;
  }

  set numColumns(numColumns: number) {
    this._numColumns = numColumns;
  }
}
