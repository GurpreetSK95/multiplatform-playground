//
//  PokeListViewController+UITableView.swift
//  iosApp
//
//  Created by Stefano Venturin on 08/01/2019.
//

import UIKit
import app

extension PokeListViewController: UITableViewDataSource, UITableViewDelegate {
    internal func update(data: [PokemonEntry]) {
        pokeList.removeAll()
        pokeList.append(contentsOf: data)
        pokeListTableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return pokeList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "pokeListCell", for: indexPath)
        let entry = pokeList[indexPath.row]
        
        cell.textLabel?.text = entry.label
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let entryNum = pokeList[indexPath.row].entry_number
        loadPokemonBy(id: entryNum)
    }
    
}
