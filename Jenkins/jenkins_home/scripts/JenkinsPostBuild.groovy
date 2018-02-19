public class JenkinsPostBuild {

  def manager

  public JenkinsPostBuild(manager){
    this.manager = manager
  }

  def run() {
    manager.listener.logger.println("I want to see this line in my job's output");
  }
}
