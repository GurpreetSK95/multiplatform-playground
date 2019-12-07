import UIKit
import app

class PokeListViewController: UIViewController {
    
    @IBOutlet var pokemonSprite: UIImageView!
    @IBOutlet var pokemonInfo: UILabel!
    
    @IBOutlet var pokeListTableView: UITableView!
    internal var pokeList: [PokemonEntry] = []
    
    internal var api = PokeApi()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        pokeListTableView.dataSource = self
        pokeListTableView.delegate = self
        
        loadList()
    }
    
    private func loadList(){
        api.getPokemonList(
            success: { data in
                self.update(data: data)
                return KotlinUnit()
        }, failure: {
            self.handleError($0?.message)
            return KotlinUnit()
        })
    }
    
    internal func loadPokemonBy(id: Int32){
        self.api.getPokemonSprite(pokemonId: id,
                                  success: { res in
                                    if let img = res {
                                        self.pokemonSprite.image = img
                                    }
                                    return KotlinUnit()
        }, failure: {
            print($0?.message ?? "Empty error")
            return KotlinUnit()
        })
        
        self.api.getPokemonInfo(pokemonId: id,
                                success: { res in
                                    self.pokemonInfo.text = res
                                    return KotlinUnit()
        },
                                failure: {
                                    self.handleError($0?.message)
                                    return KotlinUnit()
        })
    }
    
    internal func handleError(_ error: String?){
        let message = error ?? "An unknown error occurred. Retry?"
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "Retry", style: .default, handler: { action in
            self.loadList()
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        
        self.present(alert, animated: true)
    }
}
