//
//  DiaryTableViewCell.swift
//  Calendary
//
//  Created by 김기현 on 28/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit

class DiaryTableViewCell: UITableViewCell {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
