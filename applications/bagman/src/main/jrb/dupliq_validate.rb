class DupliQValidate
  
  
end


# The mid-run check:
# Disk N Rip is about to start:

# The process of validation and assurance
#
# Is the state of the content as we expect?
# Apply An Action
# Is the state as we expect?

# Given Project Identifier:
project_id = "EAP005"
# Given Output Folder:
output_folder = "C:\\DiskRobot\\EAP\\" + project_id
# Given the number of batches:
batches = "1"
# Given the disk identifiers:

# Validate initial state:
assertions do
  assert output_folder.is_folder or "Output folder #{output_folder} is missing!"
  # Check for batches:
  batches = output_folder.ls("Batch\d+")
  assert batches.count > 0 or "No batch directories could be found!"
  batches.each do |batch|  
    runs = batch.ls("Run\d+")
    assert runs.count > 0 or "No runs found for batch ${batch}!"
    # Find the last run:
    run = runs.last
    assert run.name is "Run"+runs.count or "Most recent run folder (#{run}) is not the last one! (#{runs.count})"
    assert run.ls(".*\.iso").no_checksums
  end
end  


