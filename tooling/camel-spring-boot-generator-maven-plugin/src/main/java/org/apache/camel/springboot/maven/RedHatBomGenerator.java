package org.apache.camel.springboot.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Mojo(name = "rh-generate-bom", defaultPhase = LifecyclePhase.VALIDATE, threadSafe = true)
public class RedHatBomGenerator extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true)
	protected MavenProject project;

	@Parameter(defaultValue = "${project.build.directory}/${project.name}-pom.xml")
	protected File targetPom;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			Template pomTemplate = getTemplate("redhat-bom.template");
			Map<String, String> props = new HashMap<>();
			props.put("version", project.getVersion());

			Writer file = new FileWriter(targetPom);
			pomTemplate.process(props, file);
		} catch (IOException | TemplateException ex) {
			throw new MojoExecutionException(ex.getMessage(), ex);
		}
	}

	private Template getTemplate(String name) throws IOException {
		Configuration cfg = new Configuration(Configuration.getVersion());

		cfg.setTemplateLoader(new URLTemplateLoader() {
			@Override
			protected URL getURL(String name) {
				return SpringBootStarterMojo.class.getResource("/" + name);
			}
		});

		cfg.setDefaultEncoding("UTF-8");
		Template template = cfg.getTemplate(name);
		return template;
	}
}
