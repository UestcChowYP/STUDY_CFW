class Solution {
    int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    int r, c;
    char[][] board;
    public void solve(char[][] board) {
        this.board = board;
        r = board.length;
        c = board[0].length;
        if (r <= 1) return;
        for (int i=0; i < c; i++) {
            Save(0, i);
            Save(r-1, i);
        }
        for (int i=1; i < r; i++) {
            Save(i, 0);
            Save(i, c-1);
        }
        for (int i=0; i < r; i++) {
            for (int j=0; j < c; j++) {
                if (board[i][j] == 'A') {
                    board[i][j] = 'O';
                } else if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
            }
        }
    }
    public void Save(int x, int y) {
        if (x >= 0 && y >= 0 && x < r && y < c && board[x][y] == '0'){
            board[x][y] = 'A';
            for (int[] dir:dirs) Save(x+dir[0], y + dir[1]);
        }
    }

}