results_dir = ENV['E4_RESULTS_DIR']

appkey = "vn"
envs = ["n1-u50","n1-u150","n1-u250","n2-u50","n2-u150","n2-u250","n4-u50","n4-u150","n4-u250"]

stmt_vanilla_times_taken = "SELECT CAST(ROUND(AVG(time_taken), 0) as int) as average_time_taken FROM E4Measurement WHERE virtual_user IN ('Commentor','Creator','Dashboarder','Editor','Reader','Searcher') GROUP BY virtual_user ORDER BY virtual_user;"

results = []
envs.each do |env|
  sqlite_file = "#{results_dir}/#{appkey}-#{env}/e4.sqlite"
  puts "Looking at file #{sqlite_file}"
  result = `sqlite3 -list #{sqlite_file} "#{stmt_vanilla_times_taken}"`
  result = result.split("\n")
  puts result
  result.each_with_index do |number, index|
    results[index] = [] unless results[index]
    results[index].push(number)
  end
end


results.each do |line|
  puts line.join(",")
end