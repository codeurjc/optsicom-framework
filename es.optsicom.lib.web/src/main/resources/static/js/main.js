$(document).ready(
		function() {
			$('table.display').dataTable({
				"paging" : false,
				"dom": '<"top">rt<"bottom"flp><"clear">',
				searching : false
			});

			function show_hide_column(table_id, col_no, do_hide) {

				var stl;
				if (do_hide)
					stl = 'none'
				else
					stl = 'table-cell';

				var tbl = document.getElementById(table_id);
				var rows = tbl.getElementsByTagName('tr');

				for (var row = 0; row < rows.length; row++) {
					
					var cels = rows[row].getElementsByTagName('td');
					if (!cels.length) {
						cels = rows[row].getElementsByTagName('th');
					}
					if (cels[col_no+1]) {
						cels[col_no+1].style.display = stl;
					}
				}
			}

			$('.table-col-hide').click(
					function(evt) {
						var id = $(evt.target).attr('data-target');
						$(evt.target).toggleClass('table-col-hidden');
						show_hide_column(id, $(evt.target).index(), $(
								evt.target).hasClass('table-col-hidden'));
					});

		});