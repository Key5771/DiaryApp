//
//  ListTableViewCell.swift
//  Calendary
//
//  Created by 김기현 on 23/09/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit

class ListTableViewCell: UITableViewCell {
    @IBOutlet weak var workLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
