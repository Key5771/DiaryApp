//
//  ViewController.swift
//  Calendary
//
//  Created by 김기현 on 23/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FSCalendar

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var editButton: UIButton!
    @IBOutlet weak var calendar: FSCalendar!
    @IBOutlet weak var listTableView: UITableView!
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell =  listTableView.dequeueReusableCell(withIdentifier: "listCell", for: indexPath) as! ListTableViewCell
        
        cell.workLabel.text = "완성하고싶어요"
        
        return cell
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        listTableView.delegate = self
        listTableView.dataSource = self
        
        editButton.layer.cornerRadius = 35
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        imageView.contentMode = .scaleAspectFill
        let image = UIImage(named: "Calendary.png")
        imageView.image = image
        navigationItem.titleView = imageView
        self.navigationController?.navigationBar.shadowImage = UIImage()
        
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if segue.identifier == "addDiary" {
            let addDiaryViewContollrer = segue.destination as? AddDiaryViewController
            addDiaryViewContollrer?.date = calendar.selectedDate ?? Date()
            print(calendar.selectedDate)
        }
    }

}

