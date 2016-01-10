/* @Author Lerk
 * 
 * 2111000.js: Zakum Party Quest Chest - summons 3 "Mimics"
*/

function act(){
	rm.playerMessage(5, "啊，不！！！怪物都在箱子里！");
	rm.spawnMonster(9300004,3);
}